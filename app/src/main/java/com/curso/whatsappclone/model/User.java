package com.curso.whatsappclone.model;

import com.curso.whatsappclone.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    public User(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    public void save() {
        DatabaseReference reference = FirebaseConfig.getFirebase();
        reference.child("user").child( getId() ).setValue( this );
    }

    @Exclude // Anotation para que o id não seja salvo no Firebase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude // Anotation para que o id não seja salvo no Firebase
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
