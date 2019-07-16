package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Pasillos implements Serializable {

    private Float anchura;
    private Float altura;
    private Float anchuraSalientes;
    private Float alturaSalientes;
    private Float diametroManiobra;
    private Float senializacion;
    private Boolean accesible;
    private String codCentro;
    private String id;

    public Pasillos(Float anchura, Float altura, Float anchuraSalientes, Float alturaSalientes, Float diametroManiobra, Float senializacion, Boolean accesible, String codCentro, String id) {
        this.anchura = anchura;
        this.altura = altura;
        this.anchuraSalientes = anchuraSalientes;
        this.alturaSalientes = alturaSalientes;
        this.diametroManiobra = diametroManiobra;
        this.senializacion = senializacion;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
    }

    public Pasillos() {
    }

    public Float getAnchura() {
        return anchura;
    }

    public void setAnchura(Float anchura) {
        this.anchura = anchura;
    }

    public Float getAltura() {
        return altura;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public Float getAnchuraSalientes() {
        return anchuraSalientes;
    }

    public void setAnchuraSalientes(Float anchuraSalientes) {
        this.anchuraSalientes = anchuraSalientes;
    }

    public Float getAlturaSalientes() {
        return alturaSalientes;
    }

    public void setAlturaSalientes(Float alturaSalientes) {
        this.alturaSalientes = alturaSalientes;
    }

    public Float getDiametroManiobra() {
        return diametroManiobra;
    }

    public void setDiametroManiobra(Float diametroManiobra) {
        this.diametroManiobra = diametroManiobra;
    }

    public Float getSenializacion() {
        return senializacion;
    }

    public void setSenializacion(Float senializacion) {
        this.senializacion = senializacion;
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
