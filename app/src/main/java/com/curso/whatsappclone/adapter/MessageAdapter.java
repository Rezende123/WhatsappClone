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
import com.curso.whatsappclone.activity.TalkActivity;
import com.curso.whatsappclone.model.Contact;
import com.curso.whatsappclone.model.Message;
import com.curso.whatsappclone.services.PreferenceService;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private ArrayList<Message> messages;

    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> objects) {
        super(context, 0, objects);

        this.context = context;
        this.messages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (messages != null) {
            Message message = messages.get(position);

            PreferenceService preferenceService = new PreferenceService(context);
            String idUserSender = preferenceService.getUserId();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            TextView messageText;

            if (idUserSender.equals(message.getUserId())) {
                view = inflater.inflate(R.layout.item_message_right, parent, false);
                messageText = (TextView) view.findViewById(R.id.txt_message_right);
            } else {
                view = inflater.inflate(R.layout.item_message_left, parent, false);
                messageText = (TextView) view.findViewById(R.id.txt_message_left);
            }

            messageText.setText(message.getMessage());


        }

        return view;
    }
}
