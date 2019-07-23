package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Ramps implements Serializable {

    private Float width;
    private Float length;
    private Float slope;
    private Float handRailHeight;
    private Float maneuver;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;

    public Ramps(Float width, Float length, Float slope, Float handRailHeight, Float maneuver, Boolean accessible, String centerCode, String id, String message) {
        this.width = width;
        this.length = length;
        this.slope = slope;
        this.handRailHeight = handRailHeight;
        this.maneuver = maneuver;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public Ramps() {
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getSlope() {
        return slope;
    }

    public void setSlope(Float slope) {
        this.slope = slope;
    }

    public Float getHandRailHeight() {
        return handRailHeight;
    }

    public void setHandRailHeight(Float handRailHeight) {
        this.handRailHeight = handRailHeight;
    }

    public Float getManeuver() {
        return maneuver;
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

    public void setManeuver(Float maneuver) {
        this.maneuver = maneuver;

    }

    public String getMessage(){return message;}
    public void setMessage(String message){this.message = message;}
}
