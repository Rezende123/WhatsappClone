package com.curso.whatsappclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.activity.TalkActivity;
import com.curso.whatsappclone.adapter.ConversationAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.model.Talk;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment {

    private ArrayList<Talk> talks;
    private ListView listView;
    private ArrayAdapter adapter;

    private ValueEventListener valueEventListenerTalks;
    private DatabaseReference firebase;

    @Override
    public void onStart() {
        super.onStart();

        firebase.addValueEventListener(valueEventListenerTalks);
    }

    @Override
    public void onStop() {
        super.onStop();

        firebase.removeEventListener(valueEventListenerTalks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        talks = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        listView = view.findViewById(R.id.lv_conversations);

        adapter = new ConversationAdapter(getActivity(), talks);
        listView.setAdapter(adapter);

        getTalk();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TalkActivity.class);

                Talk talk = talks.get(position);
                String email = Base64Service.decode(talk.getUserId());
                intent.putExtra("name", talk.getName());
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        return view;
    }

    private void getTalk() {
        PreferenceService preferenceService = new PreferenceService(getActivity());
        String userAuthId = preferenceService.getUserId();

        firebase = FirebaseConfig.getFirebase().child("talks").child(userAuthId);

        valueEventListenerTalks = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                talks.clear();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Talk talk = data.getValue(Talk.class);

                    talks.add(talk);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
