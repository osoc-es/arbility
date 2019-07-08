package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Profesor implements Serializable {

    private int id;
    private String nombre;
    private String codCentro;

    public Profesor(int id, String nombre, String codCentro) {
        this.id = id;
        this.nombre = nombre;
        this.codCentro = codCentro;
    }

    public Profesor() {
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }
}
