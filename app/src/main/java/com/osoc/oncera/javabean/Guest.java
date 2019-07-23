package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Guest implements Serializable {
    private String id;

    public Guest(String id) {
        this.id = id;
    }

    public Guest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

