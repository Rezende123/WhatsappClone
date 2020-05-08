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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    EditText textEmail;
    EditText textPassword;
    Button btnLogin;

    User user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifyUserAuth();

        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(
                    textEmail.getText().toString(),
                    textPassword.getText().toString()
                );

                loginValidate();
            }
        });
    }

    private void loginValidate() {
        auth = FirebaseConfig.getFirebaseAuth();

        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        openHome();
                        Toast.makeText(LoginActivity.this, "Sucesso ao efetuar o login", Toast.LENGTH_LONG).show();
                    } else {

                        String exception = "Ao efetuar o login";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            exception = "E-mail incorreto";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            exception = "Senha incorreta";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "Erro: " + exception, Toast.LENGTH_LONG).show();

                    }
                }
            });
    }

    private void verifyUserAuth() {
        auth = FirebaseConfig.getFirebaseAuth();

        if (auth.getCurrentUser() != null) {
            openHome();
        }
    }

    private void openHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void toRegister(View v) {
            Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
            startActivity(intent);
    }
}
