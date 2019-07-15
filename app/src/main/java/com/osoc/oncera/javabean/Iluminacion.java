package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Iluminacion implements Serializable {

    private Float Luz;

    public Iluminacion(Float luz) {
        Luz = luz;
    }

    public Iluminacion() {
    }

    public Float getLuz() {
        return Luz;
    }

    public void setLuz(Float luz) {
        Luz = luz;
    }
}
