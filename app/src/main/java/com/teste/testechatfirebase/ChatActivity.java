package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private GroupAdapter adapter;
    private User user;
    private User me;
    private EditText editChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getUsername());


        RecyclerView rv = findViewById(R.id.recycleChat);
        Button btnChat = findViewById(R.id.idButtonChat);
        editChat = findViewById(R.id.edtiChat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

       adapter =  new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/users")//pega a coleção de usuários
                .document(FirebaseAuth.getInstance().getUid()) // busca usuário atual , registro
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject((User.class));
                        fetchMessages(); // metodo responsavel por exibir as mensagens nos baloes


                    }
                });


    }

    private void fetchMessages() {

    if (me != null){
        String fromId = me.getUuid();//remetente
        String toId = user.getUuid();//destinatario
        FirebaseFirestore.getInstance().collection("/conversations")//lista de mensagens enviadas
                .document(fromId)
                .collection(toId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        if (documentChanges != null){
                            for (DocumentChange doc: documentChanges){
                                if (doc.getType() == DocumentChange.Type.ADDED){
                                    Message message = doc.getDocument().toObject(Message.class);
                                    adapter.add(new MessageItem(message));
                                }
                            }
                        }
                    }
                });
    }
    }

    private void sendMessage() {
        String text = editChat.getText().toString();// pega o texto digitado no chat

        editChat.setText(null); //depois de pegar o texto , apaga da memoria

        String fromId = FirebaseAuth.getInstance().getUid();

        String toId = user.getUuid();

        long timestamp = System.currentTimeMillis(); // pega o tempo da mensagem

        Message message = new Message();
        message.setFromID(fromId);
        message.setToID(toId);
        message.setTimestamp(timestamp);
        message.setText(text);

        //registrar a mensagem no firebase
        if (!message.getText().isEmpty()){
            //cria uma nova coleção no Firebase chamada coversations para registro das conversas
            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(fromId)
                    .collection(toId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste",documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste",e.getMessage(),e);
                        }
                    });
            //cria uma nova coleção no Firebase chamada coversations para registro das conversas
            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(toId)
                    .collection(fromId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste",documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste",e.getMessage(),e);
                        }
                    });


        }


    }

    //Construção de view
       private class MessageItem extends Item<ViewHolder>{

          private final Message message;

          private MessageItem(Message message){
              this.message = message;
          }

          @Override
          public void bind (@NonNull ViewHolder viewHolder,int position){
              TextView txtMsg = viewHolder.itemView.findViewById(R.id.edit_chat_from);
             ImageView imgMessage =  viewHolder.itemView.findViewById(R.id.img_message_1);

             txtMsg.setText(message.getText());
              Picasso.get()
                      .load(user.getProfileUrl())
                      .into(imgMessage);

          }
          @Override
          public int getLayout(){
              return message.getFromID().equals(FirebaseAuth.getInstance().getUid())// se usuário logado , devolve um tipo de layout se não devolve o outro
                      ? R.layout.item_editchat_from
                      : R.layout.item_editchat_to ;
          }
      }


}
