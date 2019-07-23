package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Obstacles implements Serializable {

    private String id;
    private Double length;
    private Double latitude;
    private String type;
    private String photo;
    private int order;
    private String centerCode;

    public Obstacles(String id, Double length, Double latitude, String type, String photo, int order, String centerCode) {
        this.id = id;
        this.length = length;
        this.latitude = latitude;
        this.type = type;
        this.photo = photo;
        this.order = order;
        this.centerCode = centerCode;
    }

    public Obstacles() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
}
