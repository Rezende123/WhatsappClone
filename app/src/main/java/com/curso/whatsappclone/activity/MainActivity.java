package com.curso.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.adapter.TabAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.model.User;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.curso.whatsappclone.services.SlidingTabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String contactId;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        configToolbar();

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.str_layout);
        viewPager = (ViewPager) findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_singnOut:
                singnOut();
                return true;

            case R.id.item_add_person:
                openRegisterContect();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void openRegisterContect() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário: ");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);

        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = editText.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencha o campo para cadastrar o contato", Toast.LENGTH_LONG).show();
                } else {

                    contactId = Base64Service.code(email);

                    firebase = FirebaseConfig.getFirebase();
                    firebase = firebase.child("user").child(contactId);

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                PreferenceService preferenceService = new PreferenceService(MainActivity.this);
                                String userId = preferenceService.getUserId();

                                firebase = FirebaseConfig.getFirebase();
                                firebase = firebase.child("contacts")
                                                    .child(userId)
                                                    .child(contactId);

                                User userContact = dataSnapshot.getValue(User.class);

                                Contact contact = new Contact(
                                        userContact.getName(),
                                        userContact.getEmail(),
                                        contactId
                                );

                                firebase.setValue(contact);
                                Toast.makeText(MainActivity.this, "Contato adicionado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void configToolbar() {
        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);
    }

    private void singnOut() {
        auth = FirebaseConfig.getFirebaseAuth();
        auth.signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
