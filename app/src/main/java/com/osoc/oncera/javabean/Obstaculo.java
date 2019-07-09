package com.osoc.oncera.javabean;

import java.io.Serializable;
import java.util.List;

public class Obstaculo implements Serializable
{
    private enum tipoObstaculo { ACCESO,PUERTA,PAVIMENTO,ILUMINACION,COMUNICACION,PUNT_ATENCION,
                                PASILLO,ESTANCIA,RAMPA,ASEO,ASCENSOR,SALVAESCALERAS,EVYEMERGENCIA }

    private tipoObstaculo tipoObs;
    private String codColegio;
    private String img;
    private String id;

    private List<String> idAlumnos;
    private List<Boolean> evAlumnos;

    public Obstaculo(){}

    public Obstaculo(tipoObstaculo tipoObs, String codColegio, String img, String id, List<String> idAlumnos, List<Boolean> evAlumnos) {
        this.tipoObs = tipoObs;
        this.codColegio = codColegio;
        this.img = img;
        this.id = id;
        this.idAlumnos = idAlumnos;
        this.evAlumnos = evAlumnos;
    }

    public tipoObstaculo getTipoObs() {
        return tipoObs;
    }

    public void setTipoObs(tipoObstaculo tipoObs) {
        this.tipoObs = tipoObs;
    }

    public String getCodColegio() {
        return codColegio;
    }

    public void setCodColegio(String codColegio) {
        this.codColegio = codColegio;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIdAlumnos() {
        return idAlumnos;
    }

    public void setIdAlumnos(List<String> idAlumnos) {
        this.idAlumnos = idAlumnos;
    }

    public List<Boolean> getEvAlumnos() {
        return evAlumnos;
    }

    public void setEvAlumnos(List<Boolean> evAlumnos) {
        this.evAlumnos = evAlumnos;
    }
}
