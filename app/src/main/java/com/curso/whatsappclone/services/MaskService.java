package com.curso.whatsappclone.services;

import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MaskService {
    public static void setMask(EditText editText, String mask) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mask);
        MaskTextWatcher mtw = new MaskTextWatcher(editText, smf);
        editText.addTextChangedListener(mtw);
    }
}
