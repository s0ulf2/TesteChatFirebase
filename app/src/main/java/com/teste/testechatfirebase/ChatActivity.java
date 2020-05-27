package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatActivity extends AppCompatActivity {
    private GroupAdapter adapter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getUsername());


        RecyclerView rv = findViewById(R.id.recycleChat);
       adapter =  new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


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
              return message.getFromID() == FirebaseAuth.getInstance().getUid()// se usuário logado , devolve um tipo de layout se não devolve o outro
                      ? R.layout.item_editchat_from
                      : R.layout.item_editchat_to ;
          }
      }


}
