package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Alumno implements Serializable {
    private int id;
    private String nombre;
    private String correo;
    private String codCentro;

    public Alumno(int idAlumno, String nombre, String correo, String codCentro) {
        this.id = idAlumno;
        this.nombre = nombre;
        this.correo = correo;
        this.codCentro = codCentro;
    }

    public Alumno() {
    }

    public int getIdAlumno() {
        return id;
    }

    public void setIdAlumno(int idAlumno) {
        this.id = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }
}
