package com.example.evaluadoralimentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistorialActivity extends AppCompatActivity {

    private ListView lvHistorial;
    private TextView tvMensajeVacio;
    private Button btnEliminarHistorial;
    private Button btnMenuPrincipal;
    private EncuestaStorage storage;
    private List<Encuesta> encuestas;
    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lvHistorial = findViewById(R.id.lvHistorial);
        tvMensajeVacio = findViewById(R.id.tvMensajeVacio);
        btnEliminarHistorial = findViewById(R.id.btnEliminarHistorial);
        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);
        storage = new EncuestaStorage(this);

        btnEliminarHistorial.setOnClickListener(v -> mostrarDialogoConfirmacion());

        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(HistorialActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        });

        cargarHistorial();

        lvHistorial.setOnItemClickListener((parent, view, position, id) -> {
            if (!encuestas.isEmpty()) {
                Encuesta encuestaSeleccionada = encuestas.get(position);
                Intent intent = new Intent(HistorialActivity.this, DetalleEncuestaActivity.class);
                intent.putExtra("encuesta", encuestaSeleccionada);
                startActivity(intent);
            }
        });
    }

    private void cargarHistorial() {
        encuestas = storage.obtenerEncuestas();

        if (encuestas.isEmpty()) {
            lvHistorial.setVisibility(View.GONE);
            tvMensajeVacio.setVisibility(View.VISIBLE);
            btnEliminarHistorial.setEnabled(false);
        } else {
            lvHistorial.setVisibility(View.VISIBLE);
            tvMensajeVacio.setVisibility(View.GONE);
            btnEliminarHistorial.setEnabled(true);

            adapter = new HistorialAdapter(encuestas);
            lvHistorial.setAdapter(adapter);
        }
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar historial")
                .setMessage("¿Estás seguro de que quieres eliminar TODAS las encuestas guardadas?\n\nEsta acción no se puede deshacer.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> eliminarHistorial())
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(this, "Eliminación cancelada", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void eliminarHistorial() {
        storage.limpiarHistorial();
        cargarHistorial();
        Toast.makeText(this, "Historial eliminado correctamente", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarHistorial();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistorialActivity.this, MenuPrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private class HistorialAdapter extends android.widget.BaseAdapter {
        private List<Encuesta> encuestas;
        private SimpleDateFormat sdf;

        public HistorialAdapter(List<Encuesta> encuestas) {
            this.encuestas = encuestas;
            this.sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        }

        @Override
        public int getCount() {
            return encuestas.size();
        }

        @Override
        public Object getItem(int position) {
            return encuestas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return encuestas.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_historial_item, parent, false);
            }

            Encuesta e = encuestas.get(position);

            TextView tvNombre = convertView.findViewById(R.id.tvNombre);
            TextView tvFecha = convertView.findViewById(R.id.tvFecha);
            TextView tvInfoPersonal = convertView.findViewById(R.id.tvInfoPersonal);
            TextView tvPuntaje = convertView.findViewById(R.id.tvPuntaje);
            TextView tvClasificacion = convertView.findViewById(R.id.tvClasificacion);

            String nombre = (e.getNombre() != null && !e.getNombre().isEmpty()) ? e.getNombre() : "Anónimo";
            tvNombre.setText(nombre);

            tvFecha.setText(sdf.format(e.getFecha()));

            String carrera = (e.getCarrera() != null && !e.getCarrera().isEmpty()) ? e.getCarrera() : "---";
            String sexo = (e.getSexo() != null && !e.getSexo().isEmpty()) ? e.getSexo() : "---";
            String edad = (e.getEdad() != null && !e.getEdad().isEmpty()) ? e.getEdad() : "---";
            String origen = (e.getLugarOrigen() != null && !e.getLugarOrigen().isEmpty()) ? e.getLugarOrigen() : "---";

            tvInfoPersonal.setText(carrera + " | " + sexo + " | " + edad + " años | " + origen);

            tvPuntaje.setText("Puntaje: " + e.getPuntajeTotal() + "/" + e.getMaxPuntaje());

            tvClasificacion.setText(e.getClasificacion());
            if (e.getClasificacion().equals("Alimentación Saludable")) {
                tvClasificacion.setTextColor(0xFF4CAF50);
            } else if (e.getClasificacion().equals("Necesita Cambios")) {
                tvClasificacion.setTextColor(0xFFFFC107);
            } else {
                tvClasificacion.setTextColor(0xFFEF5350);
            }

            return convertView;
        }
    }
}