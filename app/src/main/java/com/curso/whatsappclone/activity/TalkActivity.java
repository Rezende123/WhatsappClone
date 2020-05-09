package com.curso.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.adapter.MessageAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.model.Message;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Contact contact;
    private ListView listView;
    private ArrayList<Message> messages;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessage;

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
        listView = (ListView) findViewById(R.id.lv_talk);

        Bundle extras = getIntent().getExtras();

        PreferenceService preferenceService = new PreferenceService(TalkActivity.this);
        idUserSender = preferenceService.getUserId();

        if (extras != null) {
            String userId = Base64Service.code(extras.getString("email"));
            contact = new Contact(extras.getString("name"), extras.getString("email"), userId);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_talk);
        configToolbar();

        messages = new ArrayList<>();
        adapter = new MessageAdapter(TalkActivity.this, messages);
        listView.setAdapter(adapter);

        getMessages();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editMessage.getText().toString();

                if (textMessage.isEmpty()) {
                    Toast.makeText(TalkActivity.this, "Digite uma mensagem para ser enviada", Toast.LENGTH_LONG).show();
                } else {
                    Message message = new Message(idUserSender, textMessage);

                    saveMessage(contact.getUserId(), idUserSender, message);
                    saveMessage(idUserSender, contact.getUserId(), message);

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

    private boolean saveMessage(String idUserReceiver, String idUserSender, Message message) {
        try {
            firebase = FirebaseConfig.getFirebase().child("messages");

            firebase.child(idUserSender).child(idUserReceiver).push().setValue(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getMessages() {
        String idUserReceiver = contact.getUserId();
        firebase = FirebaseConfig.getFirebase().child("messages").child(idUserSender).child(idUserReceiver);

        valueEventListenerMessage = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Message currentMessage = data.getValue(Message.class);

                    messages.add(currentMessage);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMessage);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMessage);
    }
}
