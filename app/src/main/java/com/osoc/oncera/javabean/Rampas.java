package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Rampas implements Serializable {

    private Float anchura;
    private Float longitud;
    private Float pendiente;
    private Float alturaPasamanosSuperior;
    private Float privateManiobra;

    public Rampas(Float anchura, Float longitud, Float pendiente, Float alturaPasamanosSuperior, Float privateManiobra) {
        this.anchura = anchura;
        this.longitud = longitud;
        this.pendiente = pendiente;
        this.alturaPasamanosSuperior = alturaPasamanosSuperior;
        this.privateManiobra = privateManiobra;
    }

    public Rampas() {
    }

    public Float getAnchura() {
        return anchura;
    }

    public void setAnchura(Float anchura) {
        this.anchura = anchura;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public Float getPendiente() {
        return pendiente;
    }

    public void setPendiente(Float pendiente) {
        this.pendiente = pendiente;
    }

    public Float getAlturaPasamanosSuperior() {
        return alturaPasamanosSuperior;
    }

    public void setAlturaPasamanosSuperior(Float alturaPasamanosSuperior) {
        this.alturaPasamanosSuperior = alturaPasamanosSuperior;
    }

    public Float getPrivateManiobra() {
        return privateManiobra;
    }

    public void setPrivateManiobra(Float privateManiobra) {
        this.privateManiobra = privateManiobra;
    }
}
