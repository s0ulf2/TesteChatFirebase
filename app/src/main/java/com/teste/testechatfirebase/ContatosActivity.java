package com.teste.testechatfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ContatosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);
        fetchUsers(); //responsável por buscar usuários no firebase

    }
    private void fetchUsers(){
        FirebaseFirestore.getInstance().collection("/users") //Pega a coleção de usuários
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Teste", e.getMessage(), e);
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments(); // obtem cada elemento da lista
                        for (DocumentSnapshot doc:docs){
                            User user = doc.toObject((User.class));//transforma cada item encontrado em um objeto do tipo user
                            Log.d("Teste", user.getUsername());
                        }
                    }
                });
    }
}
