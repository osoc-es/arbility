package com.osoc.oncera.javabean;

import java.io.Serializable;

public class EmergencyNEvacuation implements Serializable {

    private Boolean simulation;
    private Boolean emergencyLighting;
    private Boolean accessible;
    private String centerCode;
    private String id;
    private String message;

    public EmergencyNEvacuation(Boolean simulation, Boolean emergencyLighting, Boolean accessible, String centerCode, String id, String message) {
        this.simulation = simulation;
        this.emergencyLighting = emergencyLighting;
        this.accessible = accessible;
        this.centerCode = centerCode;
        this.id = id;
        this.message = message;
    }

    public EmergencyNEvacuation() {
    }

    public Boolean getSimulation() {
        return simulation;
    }

    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    public Boolean getEmergencyLighting() {
        return emergencyLighting;
    }

    public void setEmergencyLighting(Boolean emergencyLighting) {
        this.emergencyLighting = emergencyLighting;
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
