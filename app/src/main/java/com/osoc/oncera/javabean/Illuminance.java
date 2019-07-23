package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Illuminance implements Serializable {

    private Float Ligth;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;

    public Illuminance(Float ligth, Boolean accessible, String centerCode, String id, String message) {
        Ligth = ligth;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public Illuminance() {
    }

    public Float getLigth() {
        return Ligth;
    }

    public void setLigth(Float ligth) {
        Ligth = ligth;
    }

    public Boolean getAccessible() {
        return accessible;
    }

    public void setAccessible(Boolean accessible) {
        this.accessible = accessible;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage(){return message;}
    public void setMessage(String message){this.message = message;}
}
