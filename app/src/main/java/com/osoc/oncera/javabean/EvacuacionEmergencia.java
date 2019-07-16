package com.osoc.oncera.javabean;

import java.io.Serializable;

public class EvacuacionEmergencia implements Serializable {

    private Boolean simulacros;
    private Boolean alumbradoEmergencia;
    private Boolean accesible;
    private String codCentro;
    private String id;

    public EvacuacionEmergencia(Boolean simulacros, Boolean alumbradoEmergencia, Boolean accesible, String codCentro, String id) {
        this.simulacros = simulacros;
        this.alumbradoEmergencia = alumbradoEmergencia;
        this.accesible = accesible;
        this.codCentro = codCentro;
        this.id = id;
    }

    public EvacuacionEmergencia() {
    }

    public Boolean getSimulacros() {
        return simulacros;
    }

    public void setSimulacros(Boolean simulacros) {
        this.simulacros = simulacros;
    }

    public Boolean getAlumbradoEmergencia() {
        return alumbradoEmergencia;
    }

    public void setAlumbradoEmergencia(Boolean alumbradoEmergencia) {
        this.alumbradoEmergencia = alumbradoEmergencia;
    }

    public Boolean getAccesible() {
        return accesible;
    }

    public void setAccesible(Boolean accesible) {
        this.accesible = accesible;
    }

    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
