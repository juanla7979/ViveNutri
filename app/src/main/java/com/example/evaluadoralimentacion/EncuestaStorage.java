package com.example.evaluadoralimentacion;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EncuestaStorage {
    private static final String PREF_NAME = "encuestas_pref";
    private static final String KEY_ENCUESTAS = "lista_encuestas";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public EncuestaStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
    }

    public void guardarEncuestas(List<Encuesta> encuestas) {
        String json = gson.toJson(encuestas);
        sharedPreferences.edit().putString(KEY_ENCUESTAS, json).apply();
    }

    public List<Encuesta> obtenerEncuestas() {
        String json = sharedPreferences.getString(KEY_ENCUESTAS, "");
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            Type type = new TypeToken<List<Encuesta>>(){}.getType();
            List<Encuesta> encuestas = gson.fromJson(json, type);
            return encuestas != null ? encuestas : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void agregarEncuesta(Encuesta encuesta) {
        List<Encuesta> encuestas = obtenerEncuestas();
        encuestas.add(encuesta);
        guardarEncuestas(encuestas);
    }

    public void limpiarHistorial() {
        sharedPreferences.edit().remove(KEY_ENCUESTAS).apply();
    }
}