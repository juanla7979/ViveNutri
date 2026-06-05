package com.example.evaluadoralimentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private List<Pregunta> preguntas;
    private List<Integer> respuestas;
    private int preguntaActual = 0;
    private boolean modoTabla = false;

    private TextView tvProgreso, tvPregunta, tvResultadoFinal;
    private ProgressBar progressBar;
    private RadioGroup rgOpciones;
    private HorizontalScrollView hsvTabla;
    private LinearLayout llTabla;
    private Button btnAnterior, btnSiguiente, btnVolverMenu;
    private EncuestaStorage storage;
    private List<RadioGroup> radioGroupsTabla = new ArrayList<>();

    // Opciones de frecuencia
    private final String[] opcionesFrecuencia = {"Nunca", "1-3/mes", "1-2/sem", "3-4/sem", "5-6/sem", "Todos"};
    private final int[] puntajesOpciones = {0, 1, 2, 3, 4, 5}; // para saludables
    private final int[] puntajesOpcionesNoSaludables = {5, 4, 3, 2, 1, 0};
    private UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userStorage = new UserStorage(this);

        preguntas = PreguntasData.getPreguntas();
        respuestas = new ArrayList<>(Collections.nCopies(preguntas.size(), -1));
        storage = new EncuestaStorage(this);

        initViews();
        cargarPregunta();
    }

    private void initViews() {
        tvProgreso = findViewById(R.id.tvProgreso);
        progressBar = findViewById(R.id.progressBar);
        tvPregunta = findViewById(R.id.tvPregunta);
        rgOpciones = findViewById(R.id.rgOpciones);
        hsvTabla = findViewById(R.id.hsvTabla);
        llTabla = findViewById(R.id.llTabla);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);
        tvResultadoFinal = findViewById(R.id.tvResultadoFinal);

        btnAnterior.setOnClickListener(v -> {
            guardarRespuestaActual();

            if (modoTabla) {
                // Si estoy en la tabla, regreso a la pregunta 16
                modoTabla = false;
                preguntaActual = 15; // pregunta 16 (indice 15)
            } else if (preguntaActual > 0) {
                preguntaActual--;
            }

            cargarPregunta();
        });

        btnSiguiente.setOnClickListener(v -> {
            guardarRespuestaActual();

            if (modoTabla) {
                finalizarEncuesta();
            } else if (preguntaActual == 15) {
                // Despues de pregunta 16, ir a modo tabla
                preguntaActual = 16;
                modoTabla = true;
                cargarPregunta();
            } else {
                preguntaActual++;
                cargarPregunta();
            }
        });

        btnVolverMenu.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Salir de la encuesta")
                    .setMessage("¿Estás seguro de que quieres volver al menú principal?\n\nSe perderá todo el progreso de esta encuesta.")
                    .setPositiveButton("Sí, salir", (dialogInterface, which) -> {
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });
    }

    private void guardarRespuestaActual() {
        if (modoTabla) {
            for (int i = 0; i < radioGroupsTabla.size(); i++) {
                RadioGroup rg = radioGroupsTabla.get(i);
                int selectedId = rg.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selected = findViewById(selectedId);
                    int index = rg.indexOfChild(selected);
                    int preguntaIndex = 16 + i;
                    if (preguntaIndex < respuestas.size()) {
                        respuestas.set(preguntaIndex, index);
                    }
                }
            }
        } else {
            int selectedId = rgOpciones.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selected = findViewById(selectedId);
                int index = rgOpciones.indexOfChild(selected);
                respuestas.set(preguntaActual, index);
            }
        }
    }

    private void cargarPregunta() {
        progressBar.setMax(17);

        if (modoTabla) {
            mostrarTablaPreguntas();
            return;
        }

        // Modo normal (preguntas 1 a 16)
        hsvTabla.setVisibility(View.GONE);
        rgOpciones.setVisibility(View.VISIBLE);
        tvPregunta.setVisibility(View.VISIBLE);

        Pregunta p = preguntas.get(preguntaActual);
        tvPregunta.setText(p.getTexto());
        tvProgreso.setText("Pregunta " + (preguntaActual + 1) + " de 17");
        progressBar.setProgress(preguntaActual + 1);

        rgOpciones.removeAllViews();
        String[] opciones = p.getOpciones();

        for (int i = 0; i < opciones.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opciones[i]);
            rb.setTextColor(getColor(R.color.text_primary_dark));
            rb.setId(View.generateViewId());
            rgOpciones.addView(rb);

            Integer respuestaGuardada = respuestas.get(preguntaActual);
            if (respuestaGuardada != null && respuestaGuardada != -1) {
                if (respuestaGuardada == i) {
                    rb.setChecked(true);
                }
            } else if (i == 0) {
                rb.setChecked(true);
                respuestas.set(preguntaActual, 0);
            }
        }

        actualizarEstadoBotones();
        btnSiguiente.setText(preguntaActual == 15 ? "Siguiente" : "Siguiente");
        tvResultadoFinal.setVisibility(View.GONE);
    }

    private void mostrarTablaPreguntas() {
        hsvTabla.setVisibility(View.VISIBLE);
        rgOpciones.setVisibility(View.GONE);
        tvPregunta.setVisibility(View.GONE);
        radioGroupsTabla.clear();
        llTabla.removeAllViews();

        tvProgreso.setText("Pregunta 17 de 17 - Frecuencia de alimentos");
        progressBar.setProgress(17);

        TextView tvTituloTabla = new TextView(this);
        tvTituloTabla.setText("Selecciona la frecuencia de cada alimento:");
        tvTituloTabla.setTextSize(14);
        tvTituloTabla.setTextColor(getColor(R.color.text_primary_dark));
        tvTituloTabla.setPadding(8, 8, 8, 16);
        llTabla.addView(tvTituloTabla);

        // Fila de cabeceras
        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setPadding(8, 12, 8, 12);
        headerRow.setBackgroundColor(getColor(R.color.primary_dark));

        TextView tvAlimento = new TextView(this);
        tvAlimento.setText("Alimento");
        tvAlimento.setTextSize(12);
        tvAlimento.setTextColor(getColor(R.color.background_dark));
        tvAlimento.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAlimento.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
        tvAlimento.setPadding(8, 8, 8, 8);
        headerRow.addView(tvAlimento);

        String[] opcionesAbreviadas = {"Nunca", "1-3/mes", "1-2/sem", "3-4/sem", "5-6/sem", "Diario"};
        for (String op : opcionesAbreviadas) {
            TextView tv = new TextView(this);
            tv.setText(op);
            tv.setTextSize(9);
            tv.setTextColor(getColor(R.color.background_dark));
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(2, 8, 2, 8);
            headerRow.addView(tv);
        }

        llTabla.addView(headerRow);

        // Textos abreviados de alimentos
        // Textos abreviados de alimentos en el ORDEN CORRECTO de PreguntasData (indices 16 a 39)
        String[] alimentosAbreviados = {
                "Frutas",                    // col1
                "Verduras crudas",           // col2
                "Verduras cocidas",          // col3
                "Cereales integrales",       // col4
                "Cereales refinados",        // col5
                "Leguminosas",               // col6
                "Carne roja",                // col7
                "Pollo o pavo",              // col8
                "Pescado",                   // col9
                "Mariscos",                  // col10
                "Huevos",                    // col11
                "Lacteos",                   // col12
                "Bebidas azucaradas",        // col13
                "Lechitas y cafes",          // col14
                "Licuados",                  // col15
                "Jugos naturales",           // col16
                "Snacks fritos",             // col17
                "Dulces",                    // col18
                "Postres",                   // col19
                "Ultraprocesados",           // col20
                "Frutos secos",              // col21
                "Aceites y grasas",          // col22
                "Comida rapida mexicana"     // col23
        };

        // Crear filas
        for (int i = 16; i < preguntas.size(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(8, 8, 8, 8);

            if ((i - 16) % 2 == 0) {
                row.setBackgroundColor(0x22000000);
            }

            TextView tvAlimentoItem = new TextView(this);
            tvAlimentoItem.setText(alimentosAbreviados[i - 16]);
            tvAlimentoItem.setTextSize(11);
            tvAlimentoItem.setTextColor(getColor(R.color.text_primary_dark));
            tvAlimentoItem.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
            tvAlimentoItem.setPadding(8, 8, 8, 8);
            row.addView(tvAlimentoItem);

            RadioGroup rg = new RadioGroup(this);
            rg.setOrientation(RadioGroup.HORIZONTAL);
            rg.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4.5f));

            for (int j = 0; j < opcionesFrecuencia.length; j++) {
                RadioButton rb = new RadioButton(this);
                rb.setId(View.generateViewId());
                rb.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));
                rb.setPadding(2, 4, 2, 4);
                rg.addView(rb);

                Integer respuestaGuardada = respuestas.get(i);
                if (respuestaGuardada != null && respuestaGuardada != -1 && respuestaGuardada == j) {
                    rb.setChecked(true);
                } else if (respuestaGuardada == null || respuestaGuardada == -1) {
                    if (j == 0) {
                        rb.setChecked(true);
                        respuestas.set(i, 0);
                    }
                }
            }

            row.addView(rg);
            llTabla.addView(row);
            radioGroupsTabla.add(rg);
        }

        actualizarEstadoBotones();
        btnSiguiente.setText("Finalizar");
        tvResultadoFinal.setVisibility(View.GONE);
    }

    private void actualizarEstadoBotones() {
        if (preguntaActual == 0 && !modoTabla) {
            btnAnterior.setEnabled(false);
            btnAnterior.setAlpha(0.5f);
        } else {
            btnAnterior.setEnabled(true);
            btnAnterior.setAlpha(1.0f);
        }
    }

    private void finalizarEncuesta() {
        guardarRespuestaActual();

        // Obtener datos del usuario actual
        UserData userData = userStorage.obtenerUserData();
        String nombre = userData.getNombre() != null ? userData.getNombre() : "";
        String carrera = userData.getCarrera() != null ? userData.getCarrera() : "";
        String sexo = userData.getSexo() != null ? userData.getSexo() : "";
        String lugarOrigen = userData.getLocalidad() != null ? userData.getLocalidad() : "";
        String edad = userData.getEdad() != null ? userData.getEdad() : "";

        // Reordenar respuestas SOLO para encuesta manual
        List<Integer> respuestasReordenadas = reordenarRespuestasManuales(respuestas);

        int puntajeTotal = 0;
        int maxPuntaje = 0;
        List<String> textosRespuestas = new ArrayList<>();
        List<Integer> puntajesObtenidos = new ArrayList<>();

        for (int i = 0; i < preguntas.size(); i++) {
            int respuestaIdx = respuestasReordenadas.get(i);
            Pregunta pregunta = preguntas.get(i);

            maxPuntaje += pregunta.getMaxPuntaje();

            if (respuestaIdx != -1) {
                int[] puntajes = pregunta.getPuntajes();
                if (respuestaIdx < puntajes.length) {
                    int puntajePregunta = puntajes[respuestaIdx];
                    puntajeTotal += puntajePregunta;
                    puntajesObtenidos.add(puntajePregunta);
                    String textoRespuesta = pregunta.getOpciones()[respuestaIdx];
                    textosRespuestas.add(textoRespuesta);
                }
            } else {
                puntajesObtenidos.add(0);
                textosRespuestas.add("No respondida");
            }
        }

        String clasificacion = PreguntasData.clasificarAlimentacion(puntajeTotal, maxPuntaje);

        Encuesta encuesta = new Encuesta(
                System.currentTimeMillis(),
                new Date(),
                respuestasReordenadas,
                puntajeTotal,
                maxPuntaje,
                clasificacion,
                textosRespuestas,
                puntajesObtenidos,
                nombre,
                carrera,
                sexo,
                lugarOrigen,
                edad
        );
        storage.agregarEncuesta(encuesta);

        int porcentaje = (puntajeTotal * 100) / maxPuntaje;
        String resultado = "Tu puntaje fue de " + puntajeTotal + "/" + maxPuntaje + " (" + porcentaje + "%)" + "\n\n" + clasificacion;

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Encuesta completada")
                .setMessage(resultado)
                .setPositiveButton("Realizar nueva encuesta", (dialogInterface, which) -> reiniciarEncuesta())
                .setNegativeButton("Ver historial", (dialogInterface, which) -> {
                    Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
                    startActivity(intent);
                })
                .setNeutralButton("Finalizar y volver", (dialogInterface, which) -> {
                    finish();
                })
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private List<Integer> reordenarRespuestasManuales(List<Integer> respuestasOriginales) {
        // Esta funcion solo se usa para la encuesta manual (no afecta al CSVImporter)

        List<Integer> respuestasReordenadas = new ArrayList<>(Collections.nCopies(preguntas.size(), 0));

        // Las primeras 16 preguntas ya estan en orden correcto
        for (int i = 0; i < 16; i++) {
            respuestasReordenadas.set(i, respuestasOriginales.get(i));
        }

        // Mapeo: de como se ven en la tabla (orden visual) a como deben guardarse (indice real en PreguntasData)
        // El orden VISUAL de la tabla es el mismo que alimentosAbreviados
        int[] mapeoVisualAReal = {
                16,  // Frutas
                17,  // Verduras crudas
                18,  // Verduras cocidas
                19,  // Cereales integrales
                34,  // Cereales refinados (visual col5 -> real indice 34)
                20,  // Leguminosas
                35,  // Carne roja
                21,  // Pollo
                22,  // Pescado
                36,  // Mariscos
                23,  // Huevos
                24,  // Lacteos
                37,  // Bebidas azucaradas
                38,  // Lechitas y cafes
                25,  // Licuados
                26,  // Jugos naturales
                31,  // Snacks fritos
                32,  // Dulces
                29,  // Postres
                30,  // Ultraprocesados
                27,  // Frutos secos
                28,  // Aceites y grasas
                33   // Comida rapida mexicana
        };

        for (int i = 0; i < mapeoVisualAReal.length; i++) {
            int indiceVisual = 16 + i;  // donde se guardo temporalmente la respuesta
            int indiceReal = mapeoVisualAReal[i];  // donde debe ir realmente
            if (indiceVisual < respuestasOriginales.size()) {
                respuestasReordenadas.set(indiceReal, respuestasOriginales.get(indiceVisual));
            }
        }

        return respuestasReordenadas;
    }

    private void reiniciarEncuesta() {
        respuestas = new ArrayList<>(Collections.nCopies(preguntas.size(), -1));
        preguntaActual = 0;
        modoTabla = false;
        cargarPregunta();
        tvResultadoFinal.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        guardarRespuestaActual();

        if (modoTabla) {
            modoTabla = false;
            preguntaActual = 15;
            cargarPregunta();
        } else if (preguntaActual > 0) {
            preguntaActual--;
            cargarPregunta();
        } else {
            super.onBackPressed();
        }
    }
}