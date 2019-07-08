package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Invitado implements Serializable {
    private String id;

    public Invitado(String id) {
        this.id = id;
    }

    public Invitado() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

