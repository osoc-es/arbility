package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Teacher implements Serializable {

    private String id;
    private String alias;
    private String centerCode;
    private String mail;

    public Teacher(String id, String alias, String centerCode, String mail) {
        this.id = id;
        this.alias = alias;
        this.centerCode = centerCode;
        this.mail = mail;
    }

    public Teacher() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
