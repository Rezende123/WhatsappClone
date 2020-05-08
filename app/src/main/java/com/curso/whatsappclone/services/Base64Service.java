package com.curso.whatsappclone.services;

import android.util.Base64;

public class Base64Service {

    public static String code(String text) {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode(String code) {
        return Base64.decode(code, Base64.DEFAULT).toString();
    }

}
