package com.osoc.oncera;

import java.util.HashMap;
import java.util.Map;

public final class TypesManager {

    public static final  String OBS_TYPE = "obsType";
    public static final String ILUM_OBS = "ilumObs";
    public static final String ASCENSOR_OBS = "ascensorObs";
    public static final String PUERTAS_OBS = "puertaObs";
    public static final String SALVAESC_OBS = "salvaObs";
    public static final String RAMPA_OBS = "rampaObs";




    public enum obsType {
        ASEOS(1),
        PUERTAS(2),
        ILUM(3),
        ASCENSORES(4),
        MOSTRADORES(5),
        RAMPAS(6),
        SALVAESCALERAS(7),
        ESTANCIAS(8),
        PASILLOS(9),
        EMERGENCIAS(10),
        SIMULACION(11);

        private int value;
        private static Map map = new HashMap<>();


        static {
            for (obsType pageType : obsType.values()) {
                map.put(pageType.value, pageType);
            }
        }

        private obsType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static obsType valueOf(int pageType) {
            return (obsType) map.get(pageType);
        }
    }
}
