package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Aseos implements Serializable {
    private Float diametroManiobra;
    private Float separacionBarrasApoyo;
    private Float alturaBarrasApoyo;
    private Float longitudBarraApoyo;
    private Float diametroBarraApoyo;
    private Float anchuraEspacioTransferenciaLavabo;
    private Float fondoEspacionTransferencia;
    private Float alturaHuecoLibreLavabo;
    private Boolean dispositivoLlamadaAsistencia;
    private Boolean accesoNivelDuchas;
    private Float anchuraEspacioTransferenciaDucha;
    private Float anchuraAsientoDucha;
    private Float alturaAsientoDucha;
    private Float distanciaBarraApoyoAEsquina;

    public Aseos(Float diametroManiobra, Float separacionBarrasApoyo, Float alturaBarrasApoyo, Float longitudBarraApoyo, Float diametroBarraApoyo, Float anchuraEspacioTransferenciaLavabo, Float fondoEspacionTransferencia, Float alturaHuecoLibreLavabo, Boolean dispositivoLlamadaAsistencia, Boolean accesoNivelDuchas, Float anchuraEspacioTransferenciaDucha, Float anchuraAsientoDucha, Float alturaAsientoDucha, Float distanciaBarraApoyoAEsquina) {
        this.diametroManiobra = diametroManiobra;
        this.separacionBarrasApoyo = separacionBarrasApoyo;
        this.alturaBarrasApoyo = alturaBarrasApoyo;
        this.longitudBarraApoyo = longitudBarraApoyo;
        this.diametroBarraApoyo = diametroBarraApoyo;
        this.anchuraEspacioTransferenciaLavabo = anchuraEspacioTransferenciaLavabo;
        this.fondoEspacionTransferencia = fondoEspacionTransferencia;
        this.alturaHuecoLibreLavabo = alturaHuecoLibreLavabo;
        this.dispositivoLlamadaAsistencia = dispositivoLlamadaAsistencia;
        this.accesoNivelDuchas = accesoNivelDuchas;
        this.anchuraEspacioTransferenciaDucha = anchuraEspacioTransferenciaDucha;
        this.anchuraAsientoDucha = anchuraAsientoDucha;
        this.alturaAsientoDucha = alturaAsientoDucha;
        this.distanciaBarraApoyoAEsquina = distanciaBarraApoyoAEsquina;
    }

    public Aseos() {
    }

    public Float getDiametroManiobra() {
        return diametroManiobra;
    }

    public void setDiametroManiobra(Float diametroManiobra) {
        this.diametroManiobra = diametroManiobra;
    }

    public Float getSeparacionBarrasApoyo() {
        return separacionBarrasApoyo;
    }

    public void setSeparacionBarrasApoyo(Float separacionBarrasApoyo) {
        this.separacionBarrasApoyo = separacionBarrasApoyo;
    }

    public Float getAlturaBarrasApoyo() {
        return alturaBarrasApoyo;
    }

    public void setAlturaBarrasApoyo(Float alturaBarrasApoyo) {
        this.alturaBarrasApoyo = alturaBarrasApoyo;
    }

    public Float getLongitudBarraApoyo() {
        return longitudBarraApoyo;
    }

    public void setLongitudBarraApoyo(Float longitudBarraApoyo) {
        this.longitudBarraApoyo = longitudBarraApoyo;
    }

    public Float getDiametroBarraApoyo() {
        return diametroBarraApoyo;
    }

    public void setDiametroBarraApoyo(Float diametroBarraApoyo) {
        this.diametroBarraApoyo = diametroBarraApoyo;
    }

    public Float getAnchuraEspacioTransferenciaLavabo() {
        return anchuraEspacioTransferenciaLavabo;
    }

    public void setAnchuraEspacioTransferenciaLavabo(Float anchuraEspacioTransferenciaLavabo) {
        this.anchuraEspacioTransferenciaLavabo = anchuraEspacioTransferenciaLavabo;
    }

    public Float getFondoEspacionTransferencia() {
        return fondoEspacionTransferencia;
    }

    public void setFondoEspacionTransferencia(Float fondoEspacionTransferencia) {
        this.fondoEspacionTransferencia = fondoEspacionTransferencia;
    }

    public Float getAlturaHuecoLibreLavabo() {
        return alturaHuecoLibreLavabo;
    }

    public void setAlturaHuecoLibreLavabo(Float alturaHuecoLibreLavabo) {
        this.alturaHuecoLibreLavabo = alturaHuecoLibreLavabo;
    }

    public Boolean getDispositivoLlamadaAsistencia() {
        return dispositivoLlamadaAsistencia;
    }

    public void setDispositivoLlamadaAsistencia(Boolean dispositivoLlamadaAsistencia) {
        this.dispositivoLlamadaAsistencia = dispositivoLlamadaAsistencia;
    }

    public Boolean getAccesoNivelDuchas() {
        return accesoNivelDuchas;
    }

    public void setAccesoNivelDuchas(Boolean accesoNivelDuchas) {
        this.accesoNivelDuchas = accesoNivelDuchas;
    }

    public Float getAnchuraEspacioTransferenciaDucha() {
        return anchuraEspacioTransferenciaDucha;
    }

    public void setAnchuraEspacioTransferenciaDucha(Float anchuraEspacioTransferenciaDucha) {
        this.anchuraEspacioTransferenciaDucha = anchuraEspacioTransferenciaDucha;
    }

    public Float getAnchuraAsientoDucha() {
        return anchuraAsientoDucha;
    }

    public void setAnchuraAsientoDucha(Float anchuraAsientoDucha) {
        this.anchuraAsientoDucha = anchuraAsientoDucha;
    }

    public Float getAlturaAsientoDucha() {
        return alturaAsientoDucha;
    }

    public void setAlturaAsientoDucha(Float alturaAsientoDucha) {
        this.alturaAsientoDucha = alturaAsientoDucha;
    }

    public Float getDistanciaBarraApoyoAEsquina() {
        return distanciaBarraApoyoAEsquina;
    }

    public void setDistanciaBarraApoyoAEsquina(Float distanciaBarraApoyoAEsquina) {
        this.distanciaBarraApoyoAEsquina = distanciaBarraApoyoAEsquina;
    }
}
