package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Obstaculo implements Serializable {

    private String id;
    private Double longtiud;
    private Double latitud;
    private String tipo;
    private String foto;
    private int orden;
    private String codCentro;

    public Obstaculo(String id, Double longtiud, Double latitud, String tipo, String foto, int orden, String codCentro) {
        this.id = id;
        this.longtiud = longtiud;
        this.latitud = latitud;
        this.tipo = tipo;
        this.foto = foto;
        this.orden = orden;
        this.codCentro = codCentro;
    }

    public Obstaculo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLongtiud() {
        return longtiud;
    }

    public void setLongtiud(Double longtiud) {
        this.longtiud = longtiud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }
}
