package com.osoc.oncera.javabean;

import java.io.Serializable;

public class AttPoints implements Serializable {

    private Float workFlatWidth;
    private Float workFlatHeight;
    private Float freeLowerSpaceHeight;
    private Float freeLowerSpaceWidth;
    private Float freeLowerSpaceDepth;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;

    public AttPoints(Float workFlatWidth, Float workFlatHeight, Float freeLowerSpaceHeight, Float freeLowerSpaceWidth, Float freeLowerSpaceDepth, Boolean accessible, String centerCode, String id, String message) {
        this.workFlatWidth = workFlatWidth;
        this.workFlatHeight = workFlatHeight;
        this.freeLowerSpaceHeight = freeLowerSpaceHeight;
        this.freeLowerSpaceWidth = freeLowerSpaceWidth;
        this.freeLowerSpaceDepth = freeLowerSpaceDepth;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public AttPoints() {
    }

    public Float getWorkFlatWidth() {
        return workFlatWidth;
    }

    public void setWorkFlatWidth(Float workFlatWidth) {
        this.workFlatWidth = workFlatWidth;
    }

    public Float getWorkFlatHeight() {
        return workFlatHeight;
    }

    public void setWorkFlatHeight(Float workFlatHeight) {
        this.workFlatHeight = workFlatHeight;
    }

    public Float getFreeLowerSpaceHeight() {
        return freeLowerSpaceHeight;
    }

    public void setFreeLowerSpaceHeight(Float freeLowerSpaceHeight) {
        this.freeLowerSpaceHeight = freeLowerSpaceHeight;
    }

    public Float getFreeLowerSpaceWidth() {
        return freeLowerSpaceWidth;
    }

    public void setFreeLowerSpaceWidth(Float freeLowerSpaceWidth) {
        this.freeLowerSpaceWidth = freeLowerSpaceWidth;
    }

    public Float getFreeLowerSpaceDepth() {
        return freeLowerSpaceDepth;
    }

    public void setFreeLowerSpaceDepth(Float freeLowerSpaceDepth) {
        this.freeLowerSpaceDepth = freeLowerSpaceDepth;
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
