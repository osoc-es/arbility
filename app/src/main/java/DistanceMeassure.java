package com.osoc.oncera;

public class DistanceMeassure {

    private static float pitch_a;
    private static float pitch_b;

    private static float roll_a;
    private static float roll_b;

    private static int saves;
    private static int mobile_height_centimeters = 145;

    public static void SavePoint(float pitch)
    {
        if(saves == 0) pitch_a = pitch;
        else pitch_b = pitch;

        ChangeSaves();

    }

    public static void SaveCorner(float pitch, float roll)
    {
        if(saves == 0)
        {
            pitch_a = pitch;
            roll_a = roll;
        }
        else
        {
            pitch_b = pitch;
            roll_b = roll;
        }

        ChangeSaves();
    }

    public static double GetObjectWidth()
    {
        double w_1, w_2;

        w_1 = (mobile_height_centimeters * Math.sin(Math.toRadians(roll_a)))/(Math.sin(Math.toRadians(90-pitch_a)));
        w_2 =   (mobile_height_centimeters * Math.sin(Math.toRadians(pitch_a)) * Math.sin(Math.toRadians(roll_b)))
                /
                (Math.sin(Math.toRadians(90 - pitch_a)) * Math.sin(Math.toRadians(90 - pitch_b)) * Math.sin(Math.toRadians(90-roll_b)));


        return w_1 + w_2;
    }

    public static double GetObjectHeight()
    {
        // Presuponemos que el segundo punto está por encima de la horizontal
        pitch_b -= 180;

        return mobile_height_centimeters - ((mobile_height_centimeters * Math.sin(Math.toRadians(pitch_a)) * Math.sin(Math.toRadians(90 - pitch_b)))
                                            /
                                            (Math.sin(Math.toRadians(90 - pitch_a)) * Math.sin(Math.toRadians(pitch_b)))
                                            );
    }

    public static boolean HaveAllNeededData()
    {
      return saves == 0;
    }

    public static void SetMobileHeightCentimeters(int _h)
    {
        mobile_height_centimeters = _h - 30;
    }

    private static void ChangeSaves()
    {
        saves = saves == 0 ? ++saves : 0;
    }



}
