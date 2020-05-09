package com.curso.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.services.Base64Service;

public class TalkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String userId = Base64Service.code(extras.getString("email"));
            contact = new Contact(extras.getString("name"), extras.getString("email"), userId);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_talk);
        configToolbar();
    }

    private void configToolbar() {
        toolbar.setTitle(contact.getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
    }
}
