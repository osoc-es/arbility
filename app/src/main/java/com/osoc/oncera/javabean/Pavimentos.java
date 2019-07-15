package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Pavimentos implements Serializable {
    private String imagenTipo;
    private Boolean accesible;
    private String codCentro;
    private String id;

    public Pavimentos(String imagenTipo, Boolean accesible, String codCentro, String id) {
        this.imagenTipo = imagenTipo;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
    }

    public Pavimentos() {
    }

    public String getImagenTipo() {
        return imagenTipo;
    }

    public void setImagenTipo(String imagenTipo) {
        this.imagenTipo = imagenTipo;
    }

    public Boolean getAccesible() {
        return accesible;
    }

    public void setAccesible(Boolean accesible) {
        this.accesible = accesible;
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
}
