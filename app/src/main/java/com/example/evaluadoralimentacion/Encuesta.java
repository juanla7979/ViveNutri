package com.example.evaluadoralimentacion;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Encuesta implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("fecha")
    private Date fecha;

    @SerializedName("respuestas")
    private List<Integer> respuestas;

    @SerializedName("puntajeTotal")
    private int puntajeTotal;

    @SerializedName("maxPuntaje")
    private int maxPuntaje;

    @SerializedName("clasificacion")
    private String clasificacion;

    @SerializedName("textosRespuestas")
    private List<String> textosRespuestas;

    @SerializedName("puntajesObtenidos")
    private List<Integer> puntajesObtenidos;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("carrera")
    private String carrera;

    @SerializedName("sexo")
    private String sexo;

    @SerializedName("lugarOrigen")
    private String lugarOrigen;

    @SerializedName("edad")
    private String edad;

    public Encuesta() {
    }

    public Encuesta(long id, Date fecha, List<Integer> respuestas,
                    int puntajeTotal, int maxPuntaje, String clasificacion,
                    List<String> textosRespuestas, List<Integer> puntajesObtenidos) {
        this(id, fecha, respuestas, puntajeTotal, maxPuntaje, clasificacion,
                textosRespuestas, puntajesObtenidos, "", "", "", "", "");
    }

    public Encuesta(long id, Date fecha, List<Integer> respuestas,
                    int puntajeTotal, int maxPuntaje, String clasificacion,
                    List<String> textosRespuestas, List<Integer> puntajesObtenidos,
                    String nombre, String carrera, String sexo, String lugarOrigen, String edad) {
        this.id = id;
        this.fecha = fecha;
        this.respuestas = respuestas;
        this.puntajeTotal = puntajeTotal;
        this.maxPuntaje = maxPuntaje;
        this.clasificacion = clasificacion;
        this.textosRespuestas = textosRespuestas;
        this.puntajesObtenidos = puntajesObtenidos;
        this.nombre = nombre;
        this.carrera = carrera;
        this.sexo = sexo;
        this.lugarOrigen = lugarOrigen;
        this.edad = edad;
    }

    public long getId() { return id; }
    public Date getFecha() { return fecha; }
    public List<Integer> getRespuestas() { return respuestas; }
    public int getPuntajeTotal() { return puntajeTotal; }
    public int getMaxPuntaje() { return maxPuntaje; }
    public String getClasificacion() { return clasificacion; }
    public List<String> getTextosRespuestas() { return textosRespuestas; }
    public List<Integer> getPuntajesObtenidos() { return puntajesObtenidos; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }
    public String getSexo() { return sexo; }
    public String getLugarOrigen() { return lugarOrigen; }
    public String getEdad() { return edad; }
}