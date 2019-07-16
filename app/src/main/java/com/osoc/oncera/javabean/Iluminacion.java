package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Iluminacion implements Serializable {

    private Float Luz;
    private Boolean accesible;
    private String codCentro;
    private String id;

    public Iluminacion(Float luz, Boolean accesible, String codCentro, String id) {
        Luz = luz;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
    }

    public Iluminacion() {
    }

    public Float getLuz() {
        return Luz;
    }

    public void setLuz(Float luz) {
        Luz = luz;
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
