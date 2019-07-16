package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Ascensores implements Serializable {
    private String id;
    private Float anchuraCabina;
    private Float profundidadCabina;
    private Float alturaResalte;
    private Float distanciaCabina;
    private Boolean puertasAutomaticas;
    private Boolean senialAudible;
    private Boolean botoneraBraile;
    private Boolean accesible;
    private String codCentro;


    public Ascensores(String id, Float anchuraCabina, Float profundidadCabina, Float alturaResalte, Float distanciaCabina, Boolean puertasAutomaticas, Boolean senialAudible, Boolean botoneraBraile, Boolean accesible, String codCentro) {
        this.id = id;
        this.anchuraCabina = anchuraCabina;
        this.profundidadCabina = profundidadCabina;
        this.alturaResalte = alturaResalte;
        this.distanciaCabina = distanciaCabina;
        this.puertasAutomaticas = puertasAutomaticas;
        this.senialAudible = senialAudible;
        this.botoneraBraile = botoneraBraile;
        this.accesible = accesible;
        this.codCentro = codCentro;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
