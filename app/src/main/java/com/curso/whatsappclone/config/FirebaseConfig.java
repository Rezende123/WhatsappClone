package com.curso.whatsappclone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseConfig {

    private static DatabaseReference reference;
    private static FirebaseAuth auth;

    public static  DatabaseReference getFirebase() {
        if (reference == null) {
            reference = FirebaseDatabase.getInstance().getReference();
        }

        return reference;
    }

    public static  FirebaseAuth getFirebaseAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

}
