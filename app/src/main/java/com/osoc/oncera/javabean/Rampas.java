package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Rampas implements Serializable {

    private Float anchura;
    private Float longitud;
    private Float pendiente;
    private Float alturaPasamanosSuperior;
    private Float privateManiobra;
    private Boolean accesible;
    private String codCentro;
    private String id;
    private String mensaje;

    public Rampas(Float anchura, Float longitud, Float pendiente, Float alturaPasamanosSuperior, Float privateManiobra, Boolean accesible, String codCentro, String id, String mensaje) {
        this.anchura = anchura;
        this.longitud = longitud;
        this.pendiente = pendiente;
        this.alturaPasamanosSuperior = alturaPasamanosSuperior;
        this.privateManiobra = privateManiobra;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
        this.mensaje = mensaje;
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

    public void setPrivateManiobra(Float privateManiobra) {
        this.privateManiobra = privateManiobra;

    }

    public String getMensaje(){return mensaje;}
    public void setMensaje(String mensaje){this.mensaje = mensaje;}
}
