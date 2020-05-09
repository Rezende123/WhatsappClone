package com.curso.whatsappclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.adapter.ContactAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.services.PreferenceService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contact> contacts;
    private ValueEventListener valueEventListenerContacts;

    private DatabaseReference firebase;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        firebase.addValueEventListener(valueEventListenerContacts);
    }

    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener(valueEventListenerContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contacts = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        listView = view.findViewById(R.id.lv_contacts);
        adapter = new ContactAdapter(getActivity(), contacts);
        listView.setAdapter(adapter);

        getContacts();

        return view;
    }

    private void getContacts() {
        PreferenceService preferenceService = new PreferenceService(getActivity());
        String userAuthId = preferenceService.getUserId();

        firebase = FirebaseConfig.getFirebase()
                .child("contacts").child(userAuthId);

        valueEventListenerContacts = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contacts.clear();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Contact contact = data.getValue(Contact.class);
                    contacts.add(contact);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
