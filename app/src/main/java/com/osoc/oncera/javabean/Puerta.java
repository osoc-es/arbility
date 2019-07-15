package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Puerta implements Serializable {

    private Float anchura;
    private Float altura;
    private String tipoPuerta;
    private Float alturaPomo;
    private String ImagenMecanismo;


    public Puerta(Float anchura, Float altura, String tipoPuerta, Float alturaPomo, String imagenMecanismo) {
        this.anchura = anchura;
        this.altura = altura;
        this.tipoPuerta = tipoPuerta;
        this.alturaPomo = alturaPomo;
        ImagenMecanismo = imagenMecanismo;
    }

    public Puerta() {
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

    public String getTipoPuerta() {
        return tipoPuerta;
    }

    public void setTipoPuerta(String tipoPuerta) {
        this.tipoPuerta = tipoPuerta;
    }

    public Float getAlturaPomo() {
        return alturaPomo;
    }

    public void setAlturaPomo(Float alturaPomo) {
        this.alturaPomo = alturaPomo;
    }

    public String getImagenMecanismo() {
        return ImagenMecanismo;
    }

    public void setImagenMecanismo(String imagenMecanismo) {
        ImagenMecanismo = imagenMecanismo;
    }
}

