package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Estancias implements Serializable {

    private Boolean asientosReservados;
    private Float separacionMobiliario;
    private Float diametroManiobra;
    private Boolean sistemaMejoraAudicion;
    private Boolean tarimaConRampa;
    private Boolean accesible;
    private String codCentro;
    private String id;

    public Estancias(Boolean asientosReservados, Float separacionMobiliario, Float diametroManiobra, Boolean sistemaMejoraAudicion, Boolean tarimaConRampa, Boolean accesible, String codCentro, String id) {
        this.asientosReservados = asientosReservados;
        this.separacionMobiliario = separacionMobiliario;
        this.diametroManiobra = diametroManiobra;
        this.sistemaMejoraAudicion = sistemaMejoraAudicion;
        this.tarimaConRampa = tarimaConRampa;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
    }

    public Estancias() {
    }

    public Boolean getAsientosReservados() {
        return asientosReservados;
    }

    public void setAsientosReservados(Boolean asientosReservados) {
        this.asientosReservados = asientosReservados;
    }

    public Float getSeparacionMobiliario() {
        return separacionMobiliario;
    }

    public void setSeparacionMobiliario(Float separacionMobiliario) {
        this.separacionMobiliario = separacionMobiliario;
    }

    public Float getDiametroManiobra() {
        return diametroManiobra;
    }

    public void setDiametroManiobra(Float diametroManiobra) {
        this.diametroManiobra = diametroManiobra;
    }

    public Boolean getSistemaMejoraAudicion() {
        return sistemaMejoraAudicion;
    }

    public void setSistemaMejoraAudicion(Boolean sistemaMejoraAudicion) {
        this.sistemaMejoraAudicion = sistemaMejoraAudicion;
    }

    public Boolean getTarimaConRampa() {
        return tarimaConRampa;
    }

    public void setTarimaConRampa(Boolean tarimaConRampa) {
        this.tarimaConRampa = tarimaConRampa;
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
