package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.Evaluator;
import com.osoc.oncera.R;
import com.osoc.oncera.javabean.Iluminacion;

import java.text.DecimalFormat;

public class Luxometro extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {

    private SensorManager sensor_manager;
    private Sensor luxometer;
    private Spinner spinner;
    private String[] spinner_options = new String[3];
    private TextView lux_text;
    private Button button;
    private Button bt_confirmar;
    private ImageButton exit_button;
    private TextView instrucciones;

    private boolean accesible;
    private float value;
    private String type;

    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    private float max_value;
    private Iluminacion iluminacion = new Iluminacion(null, null, null, null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxometro);

        spinner_options[0] = getString(R.string.lux_exterior);
        spinner_options[1] = getString(R.string.lux_interior_escalera);
        spinner_options[2] = getString(R.string.lux_interior_habitable);

        lux_text = (TextView) findViewById(R.id.lux);
        instrucciones = (TextView) findViewById(R.id.tv_instrucciones);
        spinner = (Spinner) findViewById(R.id.lux_spinner);
        button = (Button) findViewById(R.id.BotonLuxometroMedir);
        bt_confirmar = (Button) findViewById(R.id.BotonConfirmar);
        exit_button = (ImageButton) findViewById(R.id.BotonSalir);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item,spinner_options);
        aa.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinner.setAdapter(aa);
        instrucciones.setText(getString(R.string.lux_instrucciones));

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

        bt_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Confirmar();

            }
        });

        instrucciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideInstructions();
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

        type = spinner_options[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void Medir()
    {
        lux_text.setText(form_numbers.format(value) + " lx");
        iluminacion.setLuz(value);
    }

    public void Confirmar()
    {

        if(iluminacion.getLuz() != null) {

            if (type == getString(R.string.lux_exterior)) {
                float f = GetDataFromDatabase.FloatData("Estandares/Iluminacion/Exterior");
                accesible = Evaluator.IsGreaterThan(iluminacion.getLuz(), f);

            } else if (type == getString(R.string.lux_interior_habitable)) {
                float f = GetDataFromDatabase.FloatData("Estandares/Iluminacion/InteriorHab");
                accesible = Evaluator.IsGreaterThan(iluminacion.getLuz(), f);
            } else if (type == getString(R.string.lux_interior_escalera)) {
                float m = GetDataFromDatabase.FloatData("Estandares/Iluminacion/minInteriorRE");
                float M = GetDataFromDatabase.FloatData("Estandares/Iluminacion/maxInteriorRE");

                accesible = Evaluator.IsInRange(iluminacion.getLuz(), m, M);
            }

            iluminacion.setAccesible(accesible);

            Intent i = new Intent(this,AxesibilityActivity.class);
            i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.ILUM.getValue());
            i.putExtra("ilumObs", iluminacion);

            startActivity(i);
            finish();
        }

        //TODO: Pasar a activity de mostar si es o no accesible

    }

    public void HideInstructions()
    {
        instrucciones.setVisibility(View.INVISIBLE);
    }

}
