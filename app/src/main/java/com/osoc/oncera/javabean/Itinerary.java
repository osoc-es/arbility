package com.osoc.oncera.javabean;

import java.io.Serializable;
import java.util.ArrayList;

public class Itinerary implements Serializable {

    private String id;
    private ArrayList<Obstacles> obstacles;
    private String teacherAlias;
    private String name;
    private String description;
    private String code;

    public String getItineraryCode() {
        return ItineraryCode;
    }

    public void setItineraryCode(String itineraryCode) {
        ItineraryCode = itineraryCode;
    }

    private String ItineraryCode;


    public Itinerary(String id, ArrayList<Obstacles> obstacles, String teacherAlias, String name, String description, String code, String ItineraryCode) {
        this.id = id;
        this.obstacles = obstacles;
        this.teacherAlias = teacherAlias;
        this.name = name;
        this.description = description;
        this.code = code;
        this.ItineraryCode = ItineraryCode;
    }

    public Itinerary() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Obstacles> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<Obstacles> obstacles) {
        this.obstacles = obstacles;
    }

    public String getTeacherAlias() {
        return teacherAlias;
    }

    public void setTeacherAlias(String teacherAlias) {
        this.teacherAlias = teacherAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
