package com.example.evaluadoralimentacion;

public class Pregunta {
    private String texto;
    private String[] opciones;
    private int[] puntajes;

    public Pregunta(String texto, String[] opciones, int[] puntajes) {
        this.texto = texto;
        this.opciones = opciones;
        this.puntajes = puntajes;
    }

    public String getTexto() { return texto; }
    public String[] getOpciones() { return opciones; }
    public int[] getPuntajes() { return puntajes; }

    public int getMaxPuntaje() {
        int max = 0;
        for (int p : puntajes) {
            if (p > max) {
                max = p;
            }
        }
        return max;
    }
}