package com.curso.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.User;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class UserRegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText textName;
    private EditText textEmail;
    private EditText textPassword;
    private Button btnRegister;

    private User user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        databaseReference = FirebaseConfig.getFirebase();

        textName = findViewById(R.id.textRegisterName);
        textEmail = findViewById(R.id.textRegisterEmail);
        textPassword = findViewById(R.id.textRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(
                    textName.getText().toString(),
                    textEmail.getText().toString(),
                    textPassword.getText().toString()
                );

                registerUser();
            }
        });
    }

    private void registerUser() {
        auth = FirebaseConfig.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(UserRegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserRegisterActivity.this, "Sucesso ao cadastrar o usuário", Toast.LENGTH_LONG).show();
                    String email = task.getResult().getUser().getEmail();
                    String userIdBase64 = Base64Service.code(email);

                    PreferenceService preferenceService = new PreferenceService(UserRegisterActivity.this);
                    preferenceService.saveUserPreferences(userIdBase64, user.getName());

                    user.setId(userIdBase64);
                    user.save();

                    openHome();
                } else {

                    String exception = "Ao cadastrar o usuário";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Digite uma senha mais forte, contendo letras e números";
                    }  catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "E-mail digitado é inválido, digite um novo e-mail";
                    }  catch (FirebaseAuthUserCollisionException e) {
                        exception = "E-mail já está em uso";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(UserRegisterActivity.this, "Erro: " + exception, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
