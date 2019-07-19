package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Profesor implements Serializable {

    private String id;
    private String alias;
    private String codCentro;
    private String correo;

    public Profesor(String id, String alias, String codCentro, String correo) {
        this.id = id;
        this.alias = alias;
        this.codCentro = codCentro;
        this.correo = correo;
    }

    public Profesor() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
