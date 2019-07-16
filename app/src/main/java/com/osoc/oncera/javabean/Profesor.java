package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Profesor implements Serializable {

    private String id;
    private String nombre;
    private String codCentro;
    private String correo;

    public Profesor(String id, String nombre, String codCentro, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.codCentro = codCentro;
        this.correo = correo;
    }

    public Profesor() {
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
