package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.content.Intent.ACTION_PICK;

public class Main2Activity extends AppCompatActivity {

    private EditText mEditEmail;
    private EditText mEditUserName;
    private EditText mEditPassword;
    private Button mRegister;
    private Button mPhotoRegister;
    private Uri mSelectUri;
    private ImageView mImgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mEditEmail = findViewById(R.id.edit_Mail);
        mEditPassword = findViewById(R.id.edit_Password);
        mEditUserName = findViewById(R.id.edit_UserName);
        mRegister = findViewById(R.id.button_Register);
        mPhotoRegister = findViewById(R.id.Id_Button_SelectPhoto);
        mImgPhoto = findViewById(R.id.img_photo);

//chama a função createUser
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();

            }
        });
//--------------------------------------------------------------------
//Evento de click para o botão da foto
        mPhotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });



    }
//Função criada para cadastro de usuário
    private void createUser(){

        String name = mEditUserName.getText().toString();
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        //Verifica se todos os campos de cadastro foram preenchidos
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty()){
            Toast.makeText(this, "Favor preencher todos os campos!", Toast.LENGTH_SHORT).show();
        return;
        }
//----------------------------------------------------------------------------------------

//Eventos para escutar objetos do firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("Teste", task.getResult().getUser().getUid());
                        saveUserInFirebase();

                    }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste",e.getMessage());
                    }
                });
//--------------------------------------------------------------------------------------


    }
// Método para capturar resultado da seleção da foto
    protected  void onActivityResult(int requestCode, int resultcCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultcCode,data);
        if (requestCode == 0){
            mSelectUri = data.getData();
            Bitmap bitmap = null;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mSelectUri);
                mImgPhoto.setImageDrawable(new BitmapDrawable(bitmap));
                mPhotoRegister.setAlpha(0);

            }catch (IOException e ){

            }

        }
    }
//------------------------------------------------------------------------------------------

//Função para seleção de foto
     private void selectPhoto(){
        Intent intent = new Intent (ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
     }
//--------------------------------------------------------------------------------------------
//Metodo para salvar dados no Firebase
    private void saveUserInFirebase() {
        String filename = UUID.randomUUID().toString(); // gera aleatoriamente o nome do arquivo
        final StorageReference ref = FirebaseStorage.getInstance().getReference("image/" + filename);//busca imagens do dispositivo
        ref.putFile(mSelectUri) // pega a referencia da foto e informa para o firebase iniciando o upload da imagem
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("Teste", uri.toString());
                                String uid = FirebaseAuth.getInstance().getUid();//pega o user ID do usuário
                                String username = mEditUserName.getText().toString();//pega o nome do usuário
                                String profileUrl = uri.toString();//perga a url da foto

                                User user = new User(uid,username,profileUrl);//pega os objetos e instancia direto da activity user
                                //referencia do cloud firestore

                                FirebaseFirestore.getInstance().collection("users") // referencia de uma coleção
                                .add(user)// coleção
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){ // insere um novo usuário

                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.i("Teste",documentReference.getId());
                                        Intent intent =  new Intent(Main2Activity.this,MessagesActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Coloca a activity a abrir como a principal, não deixando voltar para a tela anterior , no caso
                                        //a tela de cadastro
                                        startActivity(intent);
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener(){
                                            public void onFailure(@NonNull Exception e ){
                                                Log.i("Teste", e.getMessage());
                                            }
                                        });
                            }

                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          public void onFailure(@NonNull Exception e) {
                                              Log.e("Teste", e.getMessage(), e);
                                          }
                                      }
                );
    }
}
