package com.curso.whatsappclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.activity.LoginActivity;
import com.curso.whatsappclone.activity.TalkActivity;
import com.curso.whatsappclone.adapter.ConversationAdapter;
import com.curso.whatsappclone.config.FirebaseConfig;
import com.curso.whatsappclone.model.Talk;
import com.curso.whatsappclone.services.Base64Service;
import com.curso.whatsappclone.services.PreferenceService;
import com.curso.whatsappclone.services.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment {

    private ArrayList<Talk> talks;
    private RecyclerView recyclerView;
    private ConversationAdapter adapter;

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
        recyclerView = view.findViewById(R.id.lv_conversations);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openConversation(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        adapter = new ConversationAdapter(talks);
        recyclerView.setAdapter(adapter);

        getTalk();

//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                openConversation(position);
//            }
//        });

        return view;
    }

    private void openConversation(int position) {
        Intent intent = new Intent(getActivity(), TalkActivity.class);

        Talk talk = talks.get(position);
        String email = Base64Service.decode(talk.getUserId());
        intent.putExtra("name", talk.getName());
        intent.putExtra("email", email);

        startActivity(intent);
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

                Log.v("SIZE", String.valueOf(talks.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
}
