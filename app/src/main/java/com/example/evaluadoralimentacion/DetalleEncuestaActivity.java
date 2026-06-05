package com.example.evaluadoralimentacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetalleEncuestaActivity extends AppCompatActivity {

    private Button btnMenuPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_encuesta);

        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);
        btnMenuPrincipal.setOnClickListener(v -> finish());

        Encuesta encuesta = (Encuesta) getIntent().getSerializableExtra("encuesta");

        if (encuesta != null) {
            mostrarDetalle(encuesta);
        }
    }

    private void mostrarDetalle(Encuesta encuesta) {
        TextView tvNombre = findViewById(R.id.tvNombreDetalle);
        TextView tvInfoPersonal = findViewById(R.id.tvInfoPersonalDetalle);
        TextView tvFecha = findViewById(R.id.tvFechaDetalle);
        TextView tvPuntaje = findViewById(R.id.tvPuntajeDetalle);
        LinearLayout llDetalle = findViewById(R.id.llDetalleRespuestas);

        llDetalle.removeAllViews();

        String nombre = (encuesta.getNombre() != null && !encuesta.getNombre().isEmpty()) ? encuesta.getNombre() : "Anónimo";
        tvNombre.setText(nombre);

        String carrera = (encuesta.getCarrera() != null && !encuesta.getCarrera().isEmpty()) ? encuesta.getCarrera() : "---";
        String sexo = (encuesta.getSexo() != null && !encuesta.getSexo().isEmpty()) ? encuesta.getSexo() : "---";
        String edad = (encuesta.getEdad() != null && !encuesta.getEdad().isEmpty()) ? encuesta.getEdad() : "---";
        String origen = (encuesta.getLugarOrigen() != null && !encuesta.getLugarOrigen().isEmpty()) ? encuesta.getLugarOrigen() : "---";

        tvInfoPersonal.setText(carrera + " | " + sexo + " | " + edad + " años | " + origen);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        tvFecha.setText("Fecha: " + sdf.format(encuesta.getFecha()));

        String puntajeText = "Puntaje: " + encuesta.getPuntajeTotal() + "/" + encuesta.getMaxPuntaje();
        tvPuntaje.setText(puntajeText);

        if (encuesta.getClasificacion().equals("Alimentación Saludable")) {
            tvPuntaje.setTextColor(0xFF4CAF50);
        } else if (encuesta.getClasificacion().equals("Necesita Cambios")) {
            tvPuntaje.setTextColor(0xFFFFC107);
        } else {
            tvPuntaje.setTextColor(0xFFEF5350);
        }

        List<Pregunta> preguntas = PreguntasData.getPreguntas();
        List<String> textosRespuestas = encuesta.getTextosRespuestas();
        List<Integer> puntajesObtenidos = encuesta.getPuntajesObtenidos();

        if (textosRespuestas == null || puntajesObtenidos == null) {
            return;
        }

        for (int i = 0; i < preguntas.size() && i < textosRespuestas.size(); i++) {
            Pregunta pregunta = preguntas.get(i);
            String respuesta = textosRespuestas.get(i);
            int puntaje = puntajesObtenidos.get(i);
            int[] puntajesPosibles = pregunta.getPuntajes();

            LinearLayout preguntaLayout = new LinearLayout(this);
            preguntaLayout.setOrientation(LinearLayout.VERTICAL);
            preguntaLayout.setPadding(16, 16, 16, 16);
            preguntaLayout.setBackgroundColor(0xFF2D2D2D);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            preguntaLayout.setLayoutParams(params);

            TextView tvPregunta = new TextView(this);
            tvPregunta.setText((i + 1) + ". " + pregunta.getTexto());
            tvPregunta.setTextSize(14);
            tvPregunta.setTextColor(0xFFFFFFFF);
            tvPregunta.setPadding(8, 4, 8, 4);

            TextView tvRespuesta = new TextView(this);
            tvRespuesta.setText("Respuesta: " + respuesta);
            tvRespuesta.setTextSize(13);
            tvRespuesta.setTextColor(0xFFBB86FC);
            tvRespuesta.setPadding(8, 4, 8, 4);

            TextView tvPuntajePregunta = new TextView(this);
            String posibles = "";
            for (int j = 0; j < puntajesPosibles.length; j++) {
                if (j > 0) posibles += " | ";
                posibles += puntajesPosibles[j];
            }
            tvPuntajePregunta.setText("Puntaje: " + puntaje);
            tvPuntajePregunta.setTextSize(12);
            tvPuntajePregunta.setTextColor(0xFF4CAF50);
            tvPuntajePregunta.setPadding(8, 4, 8, 4);

            preguntaLayout.addView(tvPregunta);
            preguntaLayout.addView(tvRespuesta);
            preguntaLayout.addView(tvPuntajePregunta);

            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));
            separator.setBackgroundColor(0xFF555555);
            preguntaLayout.addView(separator);

            llDetalle.addView(preguntaLayout);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}