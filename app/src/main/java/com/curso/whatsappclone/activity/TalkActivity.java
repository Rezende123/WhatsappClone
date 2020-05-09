package com.curso.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.model.Message;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.google.firebase.database.DatabaseReference;

public class TalkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Contact contact;

    private EditText editMessage;
    private ImageButton sendMessage;

    private String idUserSender;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        editMessage = (EditText) findViewById(R.id.edit_message);
        sendMessage = (ImageButton) findViewById(R.id.send_message);

        Bundle extras = getIntent().getExtras();

        PreferenceService preferenceService = new PreferenceService(TalkActivity.this);
        idUserSender = preferenceService.getUserId();

        if (extras != null) {
            String userId = Base64Service.code(extras.getString("email"));
            contact = new Contact(extras.getString("name"), extras.getString("email"), userId);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_talk);
        configToolbar();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editMessage.getText().toString();

                if (textMessage.isEmpty()) {
                    Toast.makeText(TalkActivity.this, "Digite uma mensagem para ser enviada", Toast.LENGTH_LONG).show();
                } else {
                    Message message = new Message(idUserSender, textMessage);

                    saveMessage(message);

                    editMessage.setText("");
                }
            }
        });
    }

    private void configToolbar() {
        toolbar.setTitle(contact.getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
    }

    private boolean saveMessage(Message message) {
        try {
            firebase = FirebaseConfig.getFirebase().child("messages");
            String idUserReceiver = contact.getUserId();

            firebase.child(idUserSender).child(idUserReceiver).push().setValue(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
