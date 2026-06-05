package com.example.evaluadoralimentacion;

import java.io.Serializable;

public class UserData implements Serializable {
    private String nombre;
    private String edad;
    private String carrera;
    private String localidad;
    private String sexo;

    public UserData() {}

    public UserData(String nombre, String edad, String carrera, String localidad, String sexo) {
        this.nombre = nombre;
        this.edad = edad;
        this.carrera = carrera;
        this.localidad = localidad;
        this.sexo = sexo;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public boolean isEmpty() {
        return (nombre == null || nombre.isEmpty()) &&
                (edad == null || edad.isEmpty()) &&
                (carrera == null || carrera.isEmpty()) &&
                (localidad == null || localidad.isEmpty()) &&
                (sexo == null || sexo.isEmpty());
    }
}