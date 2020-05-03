package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Declarando componentes
    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mBtnEnter;
    private TextView mTxtAccount;
    //-----------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linkando componentes com as variáveis//
        mEditEmail = findViewById(R.id.edit_Mail);
        mEditPassword = findViewById(R.id.edit_Password);
        mBtnEnter = findViewById(R.id.button_Login);
        mTxtAccount = findViewById(R.id.text_Warning);
        //----------------------------------------//

        //Configuração de acão ao clicar o botão Enter//
        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Criando referencia para captura
                String email = mEditEmail.getText().toString();
                String password = mEditPassword.getText().toString();
                // Teste de login de usuário

                Log.i("Teste",email);
                Log.i("Teste",password);
                //Verifica se todos os campos de cadastro foram preenchidos
                if (email == null || email.isEmpty() || password == null || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Favor preencher todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               Log.i("Teste",task.getResult().getUser().getUid());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Teste",e.getMessage());
                            }
                        });
//--------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------

            }
        });
        //------------------------------------------------------//
        //Configurando ao clicar no texto de cadastro a troca de tela//
        mTxtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui sinaliza a intenção , onde MainActivity.this informa de onde está e Main2Activity.class informa para onde quer ir //
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        //---------------------------------------------------------//

    }
}
