package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.Rampas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeasureInclination extends AppCompatActivity {

    SensorManager sManager;

    private TextView pitch_text;
    private Button btnMeasure;
    private ImageButton btnBack;

    private boolean railing;
    private String message;

    private DecimalFormat df = new DecimalFormat("#0.00º");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_inclination);

        getDBValues();

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        pitch_text = (TextView) findViewById(R.id.pitch);
        btnMeasure = (Button) findViewById(R.id.btn_measure_inclination);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Medir();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        _ramp = (Rampas) getIntent().getExtras().get("rampaIntermedio");
        railing = getIntent().getBooleanExtra("railing",false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
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

    private class casos {

        public casos(float longitude, float slope, float minLong, float maxLong) {
            this.longitude = longitude;
            this.slope = slope;
            this.minLong = minLong;
            this.maxLong = maxLong;
        }

        public float getLongitude() {
            return longitude;
        }

        public float getSlope() {
            return slope;
        }

        public float getMinLong() {
            return minLong;
        }

        public float getMaxLong() {
            return maxLong;
        }

        private float longitude, slope, minLong, maxLong;

    }

    private casos ramp1, ramp2, ramp3;
    private float paramWidth;
    private float minParamKnob, maxParamKnob;
    private Rampas _ramp;


    
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
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X, SensorManager.AXIS_Y, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * 57.2957795f;
                pitch = Math.abs(values[1] * 57.2957795f);
                roll = values[2] * 57.2957795f;
                mags = null;
                accels = null;
            }
        }
    };

    /**
     * Get and display the slope value and evaluate whether it is accessible
     */
    private void Medir() {
        pitch_text.setText(df.format(pitch));
        _ramp.setPendiente(pitch);

       evaluate();
    }

    /**
     * Get values of accessibility standards from database
     */
    private void getDBValues() {
        final DatabaseReference anchDB = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/Anchura");

        anchDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paramWidth = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference cas1 = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/Caso1");

        cas1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Float> data = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getValue(Float.class));
                }
                ramp1 = new casos(data.get(0), data.get(1), -1, -1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference cas2 = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/Caso2");

        cas2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Float> data = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getValue(Float.class));
                }
                ramp2 = new casos(-1, data.get(0), data.get(1), data.get(2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference cas3 = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/Caso3");

        cas3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Float> data = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getValue(Float.class));
                }
                ramp3 = new casos(-1, data.get(0), data.get(1), data.get(2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference minPomo = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/minAlturaPasamanos");

        minPomo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minParamKnob = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference maxPomo = FirebaseDatabase.getInstance().getReference("Estandares/Rampas/maxAlturaPasamanos");

        maxPomo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxParamKnob = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Check whether the counter is accessible or not and start Axesibility activity to display the
     * result
     */
    private void evaluate() {
        String s ="";

       boolean cumpleAnch = Evaluator.IsGreaterThan(_ramp.getAnchura(), paramWidth);

        s = UpdateStringIfNeeded(s, getString(R.string.mostr_n_anpt) + paramWidth, cumpleAnch);

        casos aux;

        float lng = _ramp.getLongitud();

        if(lng < ramp1.getLongitude()) aux = ramp1;
        else if(Evaluator.IsInRange(lng, ramp2.getMinLong(), ramp2.getMaxLong())) aux = ramp2;
        else aux = ramp3;

        boolean cumpleIncl = Evaluator.IsLowerThan(_ramp.getPendiente(),aux.getSlope());
        s = UpdateStringIfNeeded(s, "y", s == "" || cumpleIncl);
        s = UpdateStringIfNeeded(s, getString(R.string.ramp_n_incl) + aux.getSlope(), cumpleIncl);



        boolean cumplePasamanos;

        if(railing)
            cumplePasamanos = Evaluator.IsInRange(_ramp.getAlturaPasamanosSuperior(), minParamKnob, maxParamKnob);

        else cumplePasamanos = true;

        s = UpdateStringIfNeeded(s, "y", s == "" || cumplePasamanos);
        s = UpdateStringIfNeeded(s, getString(R.string.ramp_n_alt) + aux.getSlope(), cumplePasamanos);

        _ramp.setAccesible(cumpleAnch && cumpleIncl && cumplePasamanos);

        UpdateMessage(_ramp.getAccesible(), s);

        _ramp.setMensaje(message);

        Intent i = new Intent(this,AxesibilityActivity.class);
        i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.RAMPAS.getValue());
        i.putExtra(TypesManager.RAMPA_OBS, _ramp);

        startActivity(i);
        finish();
    }

    /**
     * Add string to another string if a condition is met
     * @param base initial base string
     * @param to_add string that will be added to the base if the condition is met
     * @param condition condition that determines whether the string is altered or not
     * @return result string
     */
    private String UpdateStringIfNeeded(String base, String to_add, boolean condition)
    {
        return condition ? base : base + " " + to_add;
    }

    /**
     * Updates message to show whether an element is accessible or not with an explanation
     * @param condition boolean determining whether the element is accessible or not
     * @param aux explanation of the accessibility result
     */
    private void UpdateMessage(boolean condition, String aux)
    {
        message = condition? getString(R.string.accesible) : getString(R.string.no_accesible);
        message += aux;
    }

}
