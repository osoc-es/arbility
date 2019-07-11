package com.osoc.oncera.javabean;

import java.io.Serializable;

public class Centro implements Serializable {

    private String id;
    private String codCentro;
    private String nombre;
    private String correo;
    private String ciudad;
    private String direccion;
    private Boolean validar;

    public Centro(String id, String codCentro, String nombre, String correo, String ciudad, String direccion, Boolean validar) {
        this.id = id;
        this.codCentro = codCentro;
        this.nombre = nombre;
        this.correo = correo;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.validar = validar;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }
}
