package com.osoc.oncera.javabean;

import java.io.Serializable;

public class PuntosAtencion implements Serializable {

    private Float anchuraPlanoTrabajo;
    private Float alturaPlanoTrabajo;
    private Float alturaEspacioInferiorLibre;
    private Float anchuraEspacioInferiorLibre;
    private Float profundidadEspacioInferiorLibre;
    private Boolean accesible;
    private String codCentro;
    private String id;
    private String mensaje;

    public PuntosAtencion(Float anchuraPlanoTrabajo, Float alturaPlanoTrabajo, Float alturaEspacioInferiorLibre, Float anchuraEspacioInferiorLibre, Float profundidadEspacioInferiorLibre, Boolean accesible, String codCentro, String id, String mensaje) {
        this.anchuraPlanoTrabajo = anchuraPlanoTrabajo;
        this.alturaPlanoTrabajo = alturaPlanoTrabajo;
        this.alturaEspacioInferiorLibre = alturaEspacioInferiorLibre;
        this.anchuraEspacioInferiorLibre = anchuraEspacioInferiorLibre;
        this.profundidadEspacioInferiorLibre = profundidadEspacioInferiorLibre;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
        this.mensaje = mensaje;
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

    public String getMessage(){return mensaje;}
    public void setMensaje(String mensaje){this.mensaje = mensaje;}
}
