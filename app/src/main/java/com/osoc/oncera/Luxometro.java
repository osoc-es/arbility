package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.osoc.oncera.Evaluator;
import com.osoc.oncera.R;

import java.text.DecimalFormat;

public class Luxometro extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {

    private SensorManager sensor_manager;
    private Sensor luxometer;
    private Spinner spinner;
    private String[] spinner_options = new String[3];
    private TextView lux_text;
    private Button button;
    private ImageButton exit_button;

    private boolean accesible;
    private float value;

    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private float max_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxometro);

        spinner_options[0] = getString(R.string.lux_exterior);
        spinner_options[1] = getString(R.string.lux_interior_escalera);
        spinner_options[2] = getString(R.string.lux_interior_habitable);

        lux_text = (TextView) findViewById(R.id.lux);
        spinner = (Spinner) findViewById(R.id.lux_spinner);
        button = (Button) findViewById(R.id.BotonLuxometroMedir);
        exit_button = (ImageButton) findViewById(R.id.BotonSalir);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item,spinner_options);
        aa.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinner.setAdapter(aa);

        sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        luxometer = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);

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
    protected void onResume(){
        super.onResume();

        sensor_manager.registerListener(this, luxometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            value = sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if (spinner_options[i] == getString(R.string.lux_exterior)) {
            //accesible = Evaluator.IsGreaterThan(value, )

        }
        else if (spinner_options[i] == getString(R.string.lux_interior_escalera)) {
            //accesible = Evaluator.IsGreaterThan(value, )

        }
        else if (spinner_options[i] == getString(R.string.lux_interior_habitable)) {
            //accesible = Evaluator.IsGreaterThan(value, )

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void Medir()
    {
        lux_text.setText(form_numbers.format(value) + " lx");


        //TODO: Modificar valores del objeto en la BBDD
    }
}
