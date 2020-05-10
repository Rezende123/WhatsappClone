package com.curso.whatsappclone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.whatsappclone.R;
import com.curso.whatsappclone.model.Talk;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHoolder> {

    private ArrayList<Talk> talks;

    public class ViewHoolder extends RecyclerView.ViewHolder {

        TextView talkName;
        TextView talkMessage;

        public ViewHoolder(@NonNull View itemView) {
            super(itemView);

            this.talkName = itemView.findViewById(R.id.txt_talk_name);
            this.talkMessage = itemView.findViewById(R.id.txt_talk_message);
        }
    }

    public ConversationAdapter(ArrayList<Talk> talks) {
        this.talks = talks;
    }

    @NonNull
    @Override
    public ViewHoolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_talk, parent, false);

        return new ViewHoolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoolder holder, int position) {
        Talk talk = talks.get(position);

        holder.talkMessage.setText(talk.getMessage());
        holder.talkName.setText(talk.getName());
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }
}
