package com.curso.whatsappclone.services;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionService {
    public static boolean permissionValidation(int requestCode, Activity activity, String[] permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissionList = new ArrayList<String>();

            for (String permission: permissions) {
                boolean isPermited = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

                if (!isPermited) {
                    permissionList.add(permission);
                }
            }

            if (permissionList.isEmpty()) {
                return true;
            }

            String[] newPermissions = new String[permissionList.size()];

            permissionList.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        }

        return true;
    }
}
