package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private String mail;
    private String centerCode;
    private String teacherAlias;

    public Student(int idAlumno, String name, String mail, String centerCode) {
        this.id = idAlumno;
        this.name = name;
        this.mail = mail;
        this.centerCode = centerCode;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
}

