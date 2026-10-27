package com.ispc.inmosmartmobile;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class TokenStorage {

    private static final String SECURE_PREF_NAME = "sesion_secure";
    private static final String OLD_PREF_NAME = "sesion";
    private static final String KEY_TOKEN = "token";

    private static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context,
                    SECURE_PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            try {
                context.getSharedPreferences(SECURE_PREF_NAME, Context.MODE_PRIVATE).edit().clear().commit();
            } catch (Exception ignored) {}
            return null;
        }
    }

    // Guardar token e info de sesión de forma cifrada
    public static void saveSession(Context context, String token, int id) {
        SharedPreferences securePrefs = getEncryptedPrefs(context);
        if (securePrefs != null) {
            securePrefs.edit()
                    .putString(KEY_TOKEN, token)
                    .putInt("id", id)
                    .apply();
        }
        context.getSharedPreferences(OLD_PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    // Obtener el token
    public static String getToken(Context context) {
        SharedPreferences securePrefs = getEncryptedPrefs(context);
        if (securePrefs == null) return null;

        String token = securePrefs.getString(KEY_TOKEN, null);

        // MIGRACIÓN, se revisa el archivo viejo de sesion
        if (token == null) {
            SharedPreferences oldPrefs = context.getSharedPreferences(OLD_PREF_NAME, Context.MODE_PRIVATE);
            token = oldPrefs.getString(KEY_TOKEN, null);
            int id = oldPrefs.getInt("id", -1);

            if (token != null) {

                securePrefs.edit()
                        .putString(KEY_TOKEN, token)
                        .putInt("id", id)
                        .commit();

                oldPrefs.edit().clear().commit();
            }
        }
        return token;
    }

    public static void clearSession(Context context) {
        SharedPreferences securePrefs = getEncryptedPrefs(context);
        if (securePrefs != null) {
            securePrefs.edit().clear().apply();
        }
        context.getSharedPreferences(OLD_PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}