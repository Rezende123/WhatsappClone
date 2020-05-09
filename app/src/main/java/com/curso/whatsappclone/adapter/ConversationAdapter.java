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
import com.curso.whatsappclone.model.Talk;

import java.util.ArrayList;

public class ConversationAdapter extends ArrayAdapter<Talk> {

    private Context context;
    private ArrayList<Talk> talks;

    public ConversationAdapter(@NonNull Context context, @NonNull ArrayList<Talk> objects) {
        super(context, 0, objects);
        this.context = context;
        this.talks = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (talks != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_talk, parent, false);

            TextView talkName = (TextView) view.findViewById(R.id.txt_talk_name);
            TextView talkMessage = (TextView) view.findViewById(R.id.txt_talk_message);

            Talk talk = talks.get(position);

            talkName.setText(talk.getName());
            talkMessage.setText(talk.getMessage());
        }

        return view;
    }
}
