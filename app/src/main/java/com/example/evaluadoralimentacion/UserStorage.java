package com.example.evaluadoralimentacion;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class UserStorage {
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_USER_DATA = "user_data";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void guardarUserData(UserData userData) {
        String json = gson.toJson(userData);
        sharedPreferences.edit().putString(KEY_USER_DATA, json).apply();
    }

    public UserData obtenerUserData() {
        String json = sharedPreferences.getString(KEY_USER_DATA, "");
        if (json.isEmpty()) {
            return new UserData();
        }
        try {
            return gson.fromJson(json, UserData.class);
        } catch (Exception e) {
            return new UserData();
        }
    }

    public boolean existeUserData() {
        String json = sharedPreferences.getString(KEY_USER_DATA, "");
        if (json.isEmpty()) return false;
        try {
            UserData data = gson.fromJson(json, UserData.class);
            return data != null && !data.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}