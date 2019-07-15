package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Pavimentos implements Serializable {
    private String imagenTipo;

    public Pavimentos(String imagenTipo) {
        this.imagenTipo = imagenTipo;
    }

    public Pavimentos() {
    }

    public String getImagenTipo() {
        return imagenTipo;
    }

    public void setImagenTipo(String imagenTipo) {
        this.imagenTipo = imagenTipo;
    }
}
