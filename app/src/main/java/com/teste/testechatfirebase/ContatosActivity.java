package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class ContatosActivity extends AppCompatActivity {
    private GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        RecyclerView rv = findViewById(R.id.ID_Recycler);
        adapter = new GroupAdapter<>();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(ContatosActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

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
                            adapter.add(new UserItem(user));

                        }
                    }
                });
    }
    private class UserItem extends Item<ViewHolder>{
        private final User user;
        private UserItem(User user){
            this.user = user;
        }


        public void bind (ViewHolder viewHolder ,  int position){
       TextView txtUsername =  viewHolder.itemView.findViewById(R.id.textView);
       ImageView imgPhoto = viewHolder.itemView.findViewById(R.id.imageView);

       txtUsername.setText(user.getUsername()); //captura o texto digitado

       Picasso.get() // captura a foto do usuário no firebase atraves da URL
                    .load(user.getProfileUrl())
                    .into(imgPhoto);
        }
        public int getLayout(){
            return R.layout.item_user;
        }
    }

}
