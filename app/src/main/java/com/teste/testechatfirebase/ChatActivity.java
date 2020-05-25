package com.teste.testechatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Construção de view holder
      private class MessageItem extends Item<ViewHolder>{

          private final boolean isLeft;

          private MessageItem(boolean isLeft) {
              this.isLeft = isLeft;
          }

          @Override
          public void bind (@NonNull ViewHolder viewHolder,int position){

          }
          @Override
          public int getLayout(){
              return isLeft ? R.layout;
          }
      }

    }
}
