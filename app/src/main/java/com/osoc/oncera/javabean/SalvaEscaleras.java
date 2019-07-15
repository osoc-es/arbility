package com.osoc.oncera.javabean;

import java.io.Serializable;

public class SalvaEscaleras implements Serializable {

    private Float anchuraPlataforma;
    private Float largoPlataforma;
    private Boolean mandoPlataforma;
    private Boolean mandoEmbarque;
    private Boolean mandoDesembarque;
    private Boolean carga;
    private Boolean velocidad;

    public SalvaEscaleras(Float anchuraPlataforma, Float largoPlataforma, Boolean mandoPlataforma, Boolean mandoEmbarque, Boolean mandoDesembarque, Boolean carga, Boolean velocidad) {
        this.anchuraPlataforma = anchuraPlataforma;
        this.largoPlataforma = largoPlataforma;
        this.mandoPlataforma = mandoPlataforma;
        this.mandoEmbarque = mandoEmbarque;
        this.mandoDesembarque = mandoDesembarque;
        this.carga = carga;
        this.velocidad = velocidad;
    }

    public SalvaEscaleras() {
    }

    public Float getAnchuraPlataforma() {
        return anchuraPlataforma;
    }

    public void setAnchuraPlataforma(Float anchuraPlataforma) {
        this.anchuraPlataforma = anchuraPlataforma;
    }

    public Float getLargoPlataforma() {
        return largoPlataforma;
    }

    public void setLargoPlataforma(Float largoPlataforma) {
        this.largoPlataforma = largoPlataforma;
    }

    public Boolean getMandoPlataforma() {
        return mandoPlataforma;
    }

    public void setMandoPlataforma(Boolean mandoPlataforma) {
        this.mandoPlataforma = mandoPlataforma;
    }

    public Boolean getMandoEmbarque() {
        return mandoEmbarque;
    }

    public void setMandoEmbarque(Boolean mandoEmbarque) {
        this.mandoEmbarque = mandoEmbarque;
    }

    public Boolean getMandoDesembarque() {
        return mandoDesembarque;
    }

    public void setMandoDesembarque(Boolean mandoDesembarque) {
        this.mandoDesembarque = mandoDesembarque;
    }

    public Boolean getCarga() {
        return carga;
    }

    public void setCarga(Boolean carga) {
        this.carga = carga;
    }

    public Boolean getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Boolean velocidad) {
        this.velocidad = velocidad;
    }
}
