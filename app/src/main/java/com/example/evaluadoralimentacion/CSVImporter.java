package com.example.evaluadoralimentacion;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVImporter {

    private Context context;
    private EncuestaStorage storage;

    public CSVImporter(Context context) {
        this.context = context;
        this.storage = new EncuestaStorage(context);
    }

    public void importarDesdeCSV(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            String line;
            int rowCount = 0;
            int importadas = 0;

            List<Encuesta> nuevasEncuestas = new ArrayList<>();
            List<Pregunta> preguntas = PreguntasData.getPreguntas();
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm", Locale.US);

            int[] mapeoAlimentos = {
                    16, 17, 18, 19, 34, 20, 35, 21, 22, 36, 23, 24, 37, 38, 25, 26, 31, 32, 29, 30, 27, 28, 33
            };

            while ((line = reader.readLine()) != null) {
                rowCount++;
                if (rowCount == 1) continue;

                List<String> columnas = new ArrayList<>();
                StringBuilder current = new StringBuilder();
                boolean inQuotes = false;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        inQuotes = !inQuotes;
                    } else if (c == ',' && !inQuotes) {
                        columnas.add(normalizarTexto(current.toString()));
                        current = new StringBuilder();
                    } else {
                        current.append(c);
                    }
                }
                columnas.add(normalizarTexto(current.toString()));

                if (columnas.size() < 40) {
                    while (columnas.size() < 45) {
                        columnas.add("");
                    }
                }

                try {
                    String fechaStr = columnas.get(0);
                    if (fechaStr.isEmpty()) continue;
                    Date fecha = sdf.parse(fechaStr);

                    Integer[] respuestasTemp = new Integer[preguntas.size()];
                    String[] textosTemp = new String[preguntas.size()];
                    Integer[] puntajesTemp = new Integer[preguntas.size()];

                    Arrays.fill(respuestasTemp, 0);
                    Arrays.fill(puntajesTemp, 0);
                    Arrays.fill(textosTemp, "");

                    for (int i = 0; i < 16; i++) {
                        int colIndex = 24 + i;
                        String respuestaTexto = colIndex < columnas.size() ? columnas.get(colIndex) : "";
                        textosTemp[i] = respuestaTexto;
                        Pregunta pregunta = preguntas.get(i);
                        int indice = buscarIndiceRespuesta(pregunta, respuestaTexto);
                        respuestasTemp[i] = indice;
                        if (indice != -1 && indice < pregunta.getPuntajes().length) {
                            puntajesTemp[i] = pregunta.getPuntajes()[indice];
                        }
                    }

                    for (int i = 0; i < 23 && i < columnas.size() - 1; i++) {
                        int colIndex = i + 1;
                        String respuestaTexto = colIndex < columnas.size() ? columnas.get(colIndex) : "";
                        int preguntaIndex = mapeoAlimentos[i];
                        textosTemp[preguntaIndex] = respuestaTexto;
                        Pregunta pregunta = preguntas.get(preguntaIndex);
                        int indice = buscarIndiceRespuesta(pregunta, respuestaTexto);
                        respuestasTemp[preguntaIndex] = indice;
                        if (indice != -1 && indice < pregunta.getPuntajes().length) {
                            puntajesTemp[preguntaIndex] = pregunta.getPuntajes()[indice];
                        }
                    }


                    String nombre = columnas.size() > 41 ? columnas.get(41) : "";
                    String carrera = columnas.size() > 40 ? columnas.get(40) : "";
                    String sexo = columnas.size() > 42 ? columnas.get(42) : "";
                    String lugarOrigen = columnas.size() > 43 ? columnas.get(43) : "";
                    String edad = columnas.size() > 44 ? columnas.get(44) : "";

                    List<Integer> respuestasIndices = new ArrayList<>(Arrays.asList(respuestasTemp));
                    List<String> textosRespuestas = new ArrayList<>(Arrays.asList(textosTemp));
                    List<Integer> puntajesObtenidos = new ArrayList<>(Arrays.asList(puntajesTemp));

                    int puntajeTotal = 0;
                    int maxPuntaje = 0;
                    for (int i = 0; i < preguntas.size(); i++) {
                        puntajeTotal += puntajesObtenidos.get(i);
                        maxPuntaje += preguntas.get(i).getMaxPuntaje();
                    }

                    String clasificacion = PreguntasData.clasificarAlimentacion(puntajeTotal, maxPuntaje);

                    Encuesta encuesta = new Encuesta(
                            fecha.getTime(),
                            fecha,
                            respuestasIndices,
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

                    nuevasEncuestas.add(encuesta);
                    importadas++;

                } catch (Exception e) {
                    Log.e("CSV_IMPORT", "Error fila " + rowCount + ": " + e.getMessage());
                }
            }

            reader.close();

            List<Encuesta> encuestasExistentes = storage.obtenerEncuestas();
            encuestasExistentes.addAll(nuevasEncuestas);
            storage.guardarEncuestas(encuestasExistentes);

            Toast.makeText(context, "Importadas " + importadas + " de " + (rowCount - 1) + " filas", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("CSV_IMPORT", "Error: " + e.getMessage());
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        texto = texto.trim();
        texto = texto.replace("\"", "");
        texto = texto.replaceAll("^-\\s*", "");
        texto = texto.replaceAll("^#NAME\\?.*$", "");
        texto = texto.replace("�", "a").replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u");
        texto = texto.replace("S�", "Si").replace("s�", "si");
        texto = texto.replace("d�as", "dias");
        if (texto.isEmpty() || texto.equals("-") || texto.equals("=")) {
            return "";
        }
        return texto;
    }

    private int buscarIndiceRespuesta(Pregunta pregunta, String texto) {
        if (texto == null || texto.isEmpty()) return 0;

        String[] opciones = pregunta.getOpciones();
        String textoLower = texto.toLowerCase().trim();

        textoLower = textoLower.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
        textoLower = textoLower.replace("�", "a");
        textoLower = textoLower.replaceAll("^-\\s*", "");

        for (int j = 0; j < opciones.length; j++) {
            String opcionLower = opciones[j].toLowerCase();
            opcionLower = opcionLower.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
            if (opcionLower.equals(textoLower)) {
                return j;
            }
        }

        if (textoLower.contains("nunca")) return 0;
        if (textoLower.contains("1-3") || textoLower.contains("1 3") || textoLower.contains("1�3")) return 1;
        if (textoLower.contains("1-2") || textoLower.contains("1 2") || textoLower.contains("1�2")) return 2;
        if (textoLower.contains("3-4") || textoLower.contains("3 4") || textoLower.contains("3�4")) return 3;
        if (textoLower.contains("5-6") || textoLower.contains("5 6") || textoLower.contains("5�6")) return 4;
        if (textoLower.contains("todos") || textoLower.contains("diario") || textoLower.contains("todas")) return 5;
        if (textoLower.contains("si")) return 0;
        if (textoLower.contains("no")) return 1;

        return 0;
    }
}