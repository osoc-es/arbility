package com.osoc.oncera.javabean;

import java.io.Serializable;

public class StairLifter implements Serializable {

    private Float width;
    private Float length;
    private Boolean platformControl;
    private Boolean shipmentControl;
    private Boolean disembarkationControl;
    private Boolean charge;
    private Boolean velocity;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;

    public StairLifter(Float width, Float length, Boolean platformControl, Boolean shipmentControl, Boolean disembarkationControl, Boolean charge, Boolean velocity, Boolean accessible, String centerCode, String id, String message) {
        this.width = width;
        this.length = length;
        this.platformControl = platformControl;
        this.shipmentControl = shipmentControl;
        this.disembarkationControl = disembarkationControl;
        this.charge = charge;
        this.velocity = velocity;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public StairLifter() {
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

    public Boolean getPlatformControl() {
        return platformControl;
    }

    public void setPlatformControl(Boolean platformControl) {
        this.platformControl = platformControl;
    }

    public Boolean getShipmentControl() {
        return shipmentControl;
    }

    public void setShipmentControl(Boolean shipmentControl) {
        this.shipmentControl = shipmentControl;
    }

    public Boolean getDisembarkationControl() {
        return disembarkationControl;
    }

    public void setDisembarkationControl(Boolean disembarkationControl) {
        this.disembarkationControl = disembarkationControl;
    }

    public Boolean getCharge() {
        return charge;
    }

    public void setCharge(Boolean charge) {
        this.charge = charge;
    }

    public Boolean getVelocity() {
        return velocity;
    }

    public void setVelocity(Boolean velocity) {
        this.velocity = velocity;
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
