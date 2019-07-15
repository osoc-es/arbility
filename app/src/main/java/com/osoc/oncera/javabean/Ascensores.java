package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Ascensores implements Serializable {
    private Float anchuraCabina;
    private Float profundidadCabina;
    private Float alturaResalte;
    private Float distanciaCabina;
    private Boolean puertasAutomaticas;
    private Boolean senialAudible;
    private Boolean botoneraBraile;

    public Ascensores(Float anchuraCabina, Float profundidadCabina, Float alturaResalte, Float distanciaCabina, Boolean puertasAutomaticas, Boolean senialAudible, Boolean botoneraBraile) {
        this.anchuraCabina = anchuraCabina;
        this.profundidadCabina = profundidadCabina;
        this.alturaResalte = alturaResalte;
        this.distanciaCabina = distanciaCabina;
        this.puertasAutomaticas = puertasAutomaticas;
        this.senialAudible = senialAudible;
        this.botoneraBraile = botoneraBraile;
    }

    public Ascensores() {
    }

    public Float getAnchuraCabina() {
        return anchuraCabina;
    }

    public void setAnchuraCabina(Float anchuraCabina) {
        this.anchuraCabina = anchuraCabina;
    }

    public Float getProfundidadCabina() {
        return profundidadCabina;
    }

    public void setProfundidadCabina(Float profundidadCabina) {
        this.profundidadCabina = profundidadCabina;
    }

    public Float getAlturaResalte() {
        return alturaResalte;
    }

    public void setAlturaResalte(Float alturaResalte) {
        this.alturaResalte = alturaResalte;
    }

    public Float getDistanciaCabina() {
        return distanciaCabina;
    }

    public void setDistanciaCabina(Float distanciaCabina) {
        this.distanciaCabina = distanciaCabina;
    }

    public Boolean getPuertasAutomaticas() {
        return puertasAutomaticas;
    }

    public void setPuertasAutomaticas(Boolean puertasAutomaticas) {
        this.puertasAutomaticas = puertasAutomaticas;
    }

    public Boolean getSenialAudible() {
        return senialAudible;
    }

    public void setSenialAudible(Boolean senialAudible) {
        this.senialAudible = senialAudible;
    }

    public Boolean getBotoneraBraile() {
        return botoneraBraile;
    }

    public void setBotoneraBraile(Boolean botoneraBraile) {
        this.botoneraBraile = botoneraBraile;
    }
}
