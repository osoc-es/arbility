package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Institution implements Serializable {

    private String id;
    private String centerCode;
    private String name;
    private String mail;
    private String city;
    private String address;
    private Boolean validate;

    public Institution(String id, String centerCode, String name, String mail, String city, String address, Boolean validate) {
        this.id = id;
        this.centerCode = centerCode;
        this.name = name;
        this.mail = mail;
        this.city = city;
        this.address = address;
        this.validate = validate;
    }

    public Institution() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }
}
