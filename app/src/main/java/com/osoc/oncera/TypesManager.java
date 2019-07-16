package com.osoc.oncera;

import java.util.HashMap;
import java.util.Map;

public final class TypesManager {

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