package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Puerta implements Serializable {

    private int anchura;
    private int altura;
    private String tipoPuerta;
    private int alturaPomo;
    private String tipoMecanismo;
    private Boolean accesible;
    private String codCentro;
    private String id;
    private String mensaje;


    public Puerta(int anchura, int altura, String tipoPuerta, int alturaPomo, String tipoMecanismo, Boolean accesible, String codCentro, String id, String mensaje) {
        this.anchura = anchura;
        this.altura = altura;
        this.tipoPuerta = tipoPuerta;
        this.alturaPomo = alturaPomo;
        this.tipoMecanismo = tipoMecanismo;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
        this.mensaje = mensaje;
    }

    public Puerta() {
    }

    public int getAnchura() {
        return anchura;
    }

    public void setAnchura(int anchura) {
        this.anchura = anchura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getTipoPuerta() {
        return tipoPuerta;
    }

    public void setTipoPuerta(String tipoPuerta) {
        this.tipoPuerta = tipoPuerta;
    }

    public int getAlturaPomo() {
        return alturaPomo;
    }

    public void setAlturaPomo(int alturaPomo) {
        this.alturaPomo = alturaPomo;
    }

    public String getTipoMecanismo() {
        return tipoMecanismo;
    }

    public void setTipoMecanismo(String tipoMecanismo) {
        this.tipoMecanismo = tipoMecanismo;
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

