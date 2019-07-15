package com.osoc.oncera.javabean;

import java.io.Serializable;

public class EvacuacionEmergencia implements Serializable {

    private Boolean simulacros;
    private Boolean alumbradoEmergencia;

    public EvacuacionEmergencia(Boolean simulacros, Boolean alumbradoEmergencia) {
        this.simulacros = simulacros;
        this.alumbradoEmergencia = alumbradoEmergencia;
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
}
