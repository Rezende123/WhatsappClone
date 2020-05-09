package com.curso.whatsappclone.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PreferenceService {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String FILE_NAME = "WPP_CLONE_PREFERENCES";
    private final String KEY_NAME = "userId";
    private final int MODE = 0;

    public PreferenceService(Context context) {
        this.context = context;

        sharedPreferences = this.context.getSharedPreferences(FILE_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUserPreferences(String userId) {
        editor.putString(KEY_NAME, userId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public void clearPreferences() {
        editor.clear();
    }
}
