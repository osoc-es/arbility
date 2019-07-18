package com.osoc.oncera.javabean;

import java.io.Serializable;
import java.util.ArrayList;

public class Itinerario implements Serializable {

    private String id;
    private ArrayList<Obstaculo> obstaculos;
    private String aliasProfesor;
    private String nombre;
    private String descripcion;
    private String codCentro;

    public Itinerario(String id, ArrayList<Obstaculo> obstaculos, String aliasProfesor, String nombre, String descripcion, String codCentro) {
        this.id = id;
        this.obstaculos = obstaculos;
        this.aliasProfesor = aliasProfesor;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codCentro = codCentro;
    }

    public Itinerario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public void setObstaculos(ArrayList<Obstaculo> obstaculos) {
        this.obstaculos = obstaculos;
    }

    public String getAliasProfesor() {
        return aliasProfesor;
    }

    public void setAliasProfesor(String aliasProfesor) {
        this.aliasProfesor = aliasProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }
}
