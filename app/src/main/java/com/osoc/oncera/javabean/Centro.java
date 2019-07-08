package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Centro implements Serializable {

    private String id;
    private String codCentro;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String codPostal;

    public Centro(String id, String codCentro, String nombre, String direccion, String ciudad, String codPostal) {
        this.id = id;
        this.codCentro = codCentro;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codPostal = codPostal;
    }

    public Centro() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
}
