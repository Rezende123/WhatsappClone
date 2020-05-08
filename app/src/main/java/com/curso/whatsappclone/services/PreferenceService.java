package com.curso.whatsappclone.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PreferenceService {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String FILE_NAME = "WPP_CLONE_PREFERENCES";
    private final String KEY_NAME = "Name";
    private final String KEY_CELL_NUMBER = "CellNumber";
    private final String KEY_TOKEN = "Token";
    private final int MODE = 0;

    public PreferenceService(Context context) {
        this.context = context;

        sharedPreferences = this.context.getSharedPreferences(FILE_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUserPreferences(String name, String cellNumber, String token) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_CELL_NUMBER, cellNumber);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public HashMap<String, String> getUserPreferences() {
        HashMap<String, String> userPreferences = new HashMap<String, String>();

        userPreferences.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));
        userPreferences.put(KEY_CELL_NUMBER, sharedPreferences.getString(KEY_CELL_NUMBER, null));
        userPreferences.put(KEY_TOKEN, sharedPreferences.getString(KEY_TOKEN, null));

        return userPreferences;
    }

    public void clearPreferences() {
        editor.clear();
    }
}
