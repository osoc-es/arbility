package com.osoc.oncera.javabean;

import java.io.Serializable;

public class PuntosAtencion implements Serializable {

    private Float anchuraPlanoTrabajo;
    private Float alturaPlanoTrabajo;
    private Float alturaEspacioInferiorLibre;
    private Float anchuraEspacioInferiorLibre;
    private Float profundidadEspacioInferiorLibre;

    public PuntosAtencion(Float anchuraPlanoTrabajo, Float alturaPlanoTrabajo, Float alturaEspacioInferiorLibre, Float anchuraEspacioInferiorLibre, Float profundidadEspacioInferiorLibre) {
        this.anchuraPlanoTrabajo = anchuraPlanoTrabajo;
        this.alturaPlanoTrabajo = alturaPlanoTrabajo;
        this.alturaEspacioInferiorLibre = alturaEspacioInferiorLibre;
        this.anchuraEspacioInferiorLibre = anchuraEspacioInferiorLibre;
        this.profundidadEspacioInferiorLibre = profundidadEspacioInferiorLibre;
    }

    public PuntosAtencion() {
    }

    public Float getAnchuraPlanoTrabajo() {
        return anchuraPlanoTrabajo;
    }

    public void setAnchuraPlanoTrabajo(Float anchuraPlanoTrabajo) {
        this.anchuraPlanoTrabajo = anchuraPlanoTrabajo;
    }

    public Float getAlturaPlanoTrabajo() {
        return alturaPlanoTrabajo;
    }

    public void setAlturaPlanoTrabajo(Float alturaPlanoTrabajo) {
        this.alturaPlanoTrabajo = alturaPlanoTrabajo;
    }

    public Float getAlturaEspacioInferiorLibre() {
        return alturaEspacioInferiorLibre;
    }

    public void setAlturaEspacioInferiorLibre(Float alturaEspacioInferiorLibre) {
        this.alturaEspacioInferiorLibre = alturaEspacioInferiorLibre;
    }

    public Float getAnchuraEspacioInferiorLibre() {
        return anchuraEspacioInferiorLibre;
    }

    public void setAnchuraEspacioInferiorLibre(Float anchuraEspacioInferiorLibre) {
        this.anchuraEspacioInferiorLibre = anchuraEspacioInferiorLibre;
    }

    public Float getProfundidadEspacioInferiorLibre() {
        return profundidadEspacioInferiorLibre;
    }

    public void setProfundidadEspacioInferiorLibre(Float profundidadEspacioInferiorLibre) {
        this.profundidadEspacioInferiorLibre = profundidadEspacioInferiorLibre;
    }
}
