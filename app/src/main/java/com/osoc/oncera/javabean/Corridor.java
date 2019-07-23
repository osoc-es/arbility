package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Corridor implements Serializable {

    private Float width;
    private Float height;
    private Float outWidth;
    private Float outHeight;
    private Float diameter;
    private Float signposting;
    private Boolean accessible;
    private String centerCode;
    private String id;

    public Corridor(Float width, Float height, Float outWidth, Float outHeight, Float diameter, Float signposting, Boolean accessible, String centerCode, String id) {
        this.width = width;
        this.height = height;
        this.outWidth = outWidth;
        this.outHeight = outHeight;
        this.diameter = diameter;
        this.signposting = signposting;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
    }

    public Corridor() {
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getOutWidth() {
        return outWidth;
    }

    public void setOutWidth(Float outWidth) {
        this.outWidth = outWidth;
    }

    public Float getOutHeight() {
        return outHeight;
    }

    public void setOutHeight(Float outHeight) {
        this.outHeight = outHeight;
    }

    public Float getDiameter() {
        return diameter;
    }

    public void setDiameter(Float diameter) {
        this.diameter = diameter;
    }

    public Float getSignposting() {
        return signposting;
    }

    public void setSignposting(Float signposting) {
        this.signposting = signposting;
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
}
