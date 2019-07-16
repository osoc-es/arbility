package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MedirInclinacion extends AppCompatActivity {

    SensorManager sManager;

    private TextView pitch_text;
    private Button button;
    private ImageButton exit_button;

    private DecimalFormat df = new DecimalFormat("#0.00º");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medir_inclinacion);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        pitch_text = (TextView) findViewById(R.id.pitch);
        button = (Button) findViewById(R.id.BotonInclinacionMedir);
        exit_button = (ImageButton) findViewById(R.id.BotonSalir);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Medir();
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
    }


    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = event.values.clone();
                    break;
            }

            if (mags != null && accels != null) {
                gravity = new float[9];
                magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Y, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * 57.2957795f;
                pitch = Math.abs(values[1] * 57.2957795f);
                roll = values[2] * 57.2957795f;
                mags = null;
                accels = null;
            }
        }
    };

    private void Medir(){
        pitch_text.setText(df.format(pitch));
    }

}
