package com.curso.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    ArrayList<Contact> contacts;
    Context context;

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contacts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (contacts != null) {
            Contact contact = contacts.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_contact, parent, false);

            TextView contactName = (TextView) view.findViewById(R.id.txt_contact_name);
            TextView contactEmail = (TextView) view.findViewById(R.id.txt_contact_email);

            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        return view;
    }
}
