package com.osoc.oncera;

import java.util.HashMap;
import java.util.Map;


public final class TypesManager {

    public static final  String OBS_TYPE = "obsType";
    public static final String ILLUM_OBS = "Luz";
    public static final String ELEVATOR_OBS = "Ascensor";
    public static final String DOOR_OBS = "Puerta";
    public static final String STAIRLIFTER_OBS = "Salvaescaleras";
    public static final String ATTPOINT_OBS = "Mostrador";
    public static final String RAMP_OBS = "Rampa";
    public static final String EMERGENCY_OBS = "Emergencias";
    public static final String SIMULATION_OBS = "Simulacion";



    public enum obsType {
        ASEOS(1),
        DOOR(2),
        ILLUM(3),
        ELEVATOR(4),
        ATTPOINT(5),
        RAMPS(6),
        STAIRLIFTER(7),
        ROOM(8),
        CORRIDOR(9),
        EMERGENCY(10),
        SIMULATION(11);

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
