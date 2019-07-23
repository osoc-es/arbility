package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Room implements Serializable {

    private Boolean reservedSeats;
    private Float furnitureSep;
    private Float diameter;
    private Boolean audioSystem;
    private Boolean ramp;
    private Boolean accessible;
    private String centerCode;
    private String id;

    public Room(Boolean reservedSeats, Float furnitureSep, Float diameter, Boolean audioSystem, Boolean ramp, Boolean accessible, String centerCode, String id) {
        this.reservedSeats = reservedSeats;
        this.furnitureSep = furnitureSep;
        this.diameter = diameter;
        this.audioSystem = audioSystem;
        this.ramp = ramp;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
    }

    public Room() {
    }

    public Boolean getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(Boolean reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public Float getFurnitureSep() {
        return furnitureSep;
    }

    public void setFurnitureSep(Float furnitureSep) {
        this.furnitureSep = furnitureSep;
    }

    public Float getDiameter() {
        return diameter;
    }

    public void setDiameter(Float diameter) {
        this.diameter = diameter;
    }

    public Boolean getAudioSystem() {
        return audioSystem;
    }

    public void setAudioSystem(Boolean audioSystem) {
        this.audioSystem = audioSystem;
    }

    public Boolean getRamp() {
        return ramp;
    }

    public void setRamp(Boolean ramp) {
        this.ramp = ramp;
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
