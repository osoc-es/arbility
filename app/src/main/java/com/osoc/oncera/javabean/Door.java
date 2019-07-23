package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Door implements Serializable {

    private int width;
    private int height;
    private String doorType;
    private int knobHeight;
    private String mecType;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;


    public Door(int width, int height, String doorType, int knobHeight, String mecType, Boolean accessible, String centerCode, String id, String message) {
        this.width = width;
        this.height = height;
        this.doorType = doorType;
        this.knobHeight = knobHeight;
        this.mecType = mecType;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public Door() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public int getKnobHeight() {
        return knobHeight;
    }

    public void setKnobHeight(int knobHeight) {
        this.knobHeight = knobHeight;
    }

    public String getMecType() {
        return mecType;
    }

    public void setMecType(String mecType) {
        this.mecType = mecType;
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

