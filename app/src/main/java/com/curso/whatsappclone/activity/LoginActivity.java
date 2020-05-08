package com.curso.whatsappclone.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.services.MaskService;
import com.curso.whatsappclone.services.PermissionService;
import com.curso.whatsappclone.services.PreferenceService;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText textName;
    EditText textCellNumber;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textName = (EditText) findViewById(R.id.textEmail);
        textCellNumber = (EditText) findViewById(R.id.textPassword);
        btnRegister = (Button) findViewById(R.id.btnLogin);
    }

    public void toRegister(View v) {
            Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
            startActivity(intent);
            finish();
    }

    private void userRegister() {
        String name = textName.getText().toString();
        String number = "55" + textCellNumber.getText().toString();
        number = number
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", "");

        String token = generateToken().toString();

        PreferenceService preferenceService = new PreferenceService(LoginActivity.this);
        preferenceService.saveUserPreferences(name, number, token);

        boolean enviadoSMS = smsSender("+" + number, "Whatsapp Clone Codigo de confirmação: " + token);

        if (enviadoSMS) {
//            Intent intent = new Intent(LoginActivity.this, RegistrationValidatorActivity.class);
//            startActivity(intent);
//            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Problema ao mandar SMS, tente novamente", Toast.LENGTH_LONG).show();
        }
    }

    private Integer generateToken() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private boolean smsSender(String cellNumber, String message) {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(cellNumber, null, message, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result: grantResults) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                alertPermission();
            }
        }
    }

    public void alertPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permissão Negada");
        builder.setMessage("Para esse app funcionar é necessário aceitar as permissões.");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void  onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
