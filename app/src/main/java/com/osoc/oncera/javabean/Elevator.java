package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Elevator implements Serializable {
    private String id;
    private Float stallWidth;
    private Float stallDepth;
    private Float floorDiffHeight;
    private Float stallDistance;
    private Boolean autoDoors;
    private Boolean audioMark;
    private Boolean brailleButtons;
    private Boolean accessible;
    private String centerCode;
    private String message;


    public Elevator(String id, Float stallWidth, Float stallDepth, Float floorDiffHeight, Float stallDistance, Boolean autoDoors, Boolean audioMark, Boolean brailleButtons, Boolean accessible, String centerCode, String message) {
        this.id = id;
        this.stallWidth = stallWidth;
        this.stallDepth = stallDepth;
        this.floorDiffHeight = floorDiffHeight;
        this.stallDistance = stallDistance;
        this.autoDoors = autoDoors;
        this.audioMark = audioMark;
        this.brailleButtons = brailleButtons;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.message = message;
    }

    public Elevator() {
    }

    public Float getStallWidth() {
        return stallWidth;
    }

    public void setStallWidth(Float stallWidth) {
        this.stallWidth = stallWidth;
    }

    public Float getStallDepth() {
        return stallDepth;
    }

    public void setStallDepth(Float stallDepth) {
        this.stallDepth = stallDepth;
    }

    public Float getFloorDiffHeight() {
        return floorDiffHeight;
    }

    public void setFloorDiffHeight(Float floorDiffHeight) {
        this.floorDiffHeight = floorDiffHeight;
    }

    public Float getStallDistance() {
        return stallDistance;
    }

    public void setStallDistance(Float stallDistance) {
        this.stallDistance = stallDistance;
    }

    public Boolean getAutoDoors() {
        return autoDoors;
    }

    public void setAutoDoors(Boolean autoDoors) {
        this.autoDoors = autoDoors;
    }

    public Boolean getAudioMark() {
        return audioMark;
    }

    public void setAudioMark(Boolean audioMark) {
        this.audioMark = audioMark;
    }

    public Boolean getBrailleButtons() {
        return brailleButtons;
    }

    public void setBrailleButtons(Boolean brailleButtons) {
        this.brailleButtons = brailleButtons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage(){return message;}
    public void setMessage(String message){this.message = message;}
}
