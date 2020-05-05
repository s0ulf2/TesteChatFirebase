package com.teste.testechatfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        verifyAuthentication(); //Vai verificar se o usuário está logado , se estiver continua na pagina de mensagens , se não vai pra tela de login

            }
            private void verifyAuthentication(){
        if (FirebaseAuth.getInstance().getUid() == null ){//Verifica se o usuário está logado ,se o getUid for nulo significa que ele não está logado
            Intent intent = new Intent (MessagesActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
            }

    @Override// cria o menu juntamento com o arquivo xml criado
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //--------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item){// escuta eventos dentro do menu
        switch (item.getItemId()){
            case R.id.contacts:// verifica se os contatos foram acionados
                break;
            case R.id.logout: // verifica se o logout foi solicitado e informa o firebase , depois valida se ouve o logout
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
