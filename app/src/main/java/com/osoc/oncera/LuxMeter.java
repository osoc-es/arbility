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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.Illuminance;

import java.text.DecimalFormat;

/**
 * Activity that measures the light in lx using the phone sensors and evaluates its accessibility
 * using the database standards and passes to AccessibilityChecker activity.
 */
public class LuxMeter extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {

    //Sensor
    private SensorManager sensor_manager;
    private Sensor lux_meter;
    private boolean accessible = false;
    private float value;

    //Standard evaluation
    private String LocationType;
    float outDoorParam, indoorWRampParam, minIndoorH, maxIndoorH;

    //UX
    private Spinner spinner;
    private String[] spinner_options = new String[3];
    private TextView lux_text;
    private Button button;
    private Button confirmButton;
    private ImageButton exit_button;
    private TextView instructionsText;
    private String message;
    boolean instructions_hidden = false;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00");

    //Obstacle
    private Illuminance Illuminance = new Illuminance(null, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lux_meter);

        UpdateDatabaseValues();

        spinner_options[0] = getString(R.string.lux_exterior);
        spinner_options[1] = getString(R.string.lux_interior_escalera);
        spinner_options[2] = getString(R.string.lux_interior_habitable);

        lux_text = (TextView) findViewById(R.id.lux);
        instructionsText = (TextView) findViewById(R.id.tv_instrucciones);
        spinner = (Spinner) findViewById(R.id.lux_spinner);
        button = (Button) findViewById(R.id.BotonLuxometroMedir);
        confirmButton = (Button) findViewById(R.id.BotonConfirmar);
        exit_button = (ImageButton) findViewById(R.id.btnBack);


        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item,spinner_options);
        aa.setDropDownViewResource(R.layout.spinner_item_dropdown);

        spinner.setOnItemSelectedListener(this);

        spinner.setAdapter(aa);
        instructionsText.setText(getString(R.string.lux_instrucciones));

        sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lux_meter = sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Measure();
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Confirm();

            }
        });

        instructionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideInstructions();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensor_manager.registerListener(this, lux_meter, SensorManager.SENSOR_DELAY_NORMAL);
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

        LocationType = spinner_options[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * If instructions' are hidden shows the sensor value in lx and saves it into illumination obstacle
     */
    public void Measure() {
        if (instructions_hidden) {
            lux_text.setText(form_numbers.format(value) + " lx");
            Illuminance.setLigth(value);
        }
    }

    /**
     * Adds information to a message if needed
     * @param base base message to show
     * @param to_add information to add to base message
     * @param condition if it's necessary to update the message with extra info
     * @return the updated string
     */
    private String UpdateStringIfNeeded(String base, String to_add, boolean condition)
    {
        return condition ? base : base + " " + to_add;
    }

    /**
     * Updates the final obstacle message about accessibility
     * @param condition says if it is accessible or it is not
     * @param aux extra information about why is not accessible
     */
    private void UpdateMessage(boolean condition, String aux)
    {
        message = condition? getString(R.string.accessible) : getString(R.string.no_accesible);
        message += aux;
    }

    /**
     * Illumination values check based on location using standards. Accessibility's message and obstacle state update and pass to AccessibilityChecker activity
     */
    public void Confirm()
    {

    String s= "";

        if(Illuminance.getLigth() != null && instructions_hidden) {


            if (LocationType == getString(R.string.lux_exterior))
            {
                accessible = Evaluator.IsGreaterThan(Illuminance.getLigth(), outDoorParam);

                s = UpdateStringIfNeeded(s, getString(R.string.lux_n_exterior) + " "+ outDoorParam, accessible);

            }
            else if (LocationType == getString(R.string.lux_interior_habitable))
            {
                accessible = Evaluator.IsGreaterThan(Illuminance.getLigth(), indoorWRampParam);
                s = UpdateStringIfNeeded(s, getString(R.string.lux_n_int_hab) + " "+ indoorWRampParam, accessible);
            }
            else if (LocationType == getString(R.string.lux_interior_escalera))
            {
                accessible = Evaluator.IsInRange(Illuminance.getLigth(), minIndoorH, maxIndoorH);
                s = UpdateStringIfNeeded(s, getString(R.string.lux_n_int_ramp) + " "+ minIndoorH + " y " + maxIndoorH, accessible);
            }

            Illuminance.setAccessible(accessible);


            UpdateMessage(Illuminance.getAccessible(),s);
            Illuminance.setMessage(message);

            Intent i = new Intent(this,AxesibilityActivity.class);
            i.putExtra(TypesManager.OBS_TYPE,TypesManager.obsType.ILLUM.getValue());
            i.putExtra(TypesManager.ILLUM_OBS, Illuminance);

            startActivity(i);
            finish();
        }

    }

    /**
     * Hides the instructions text when clicked on
     */
    public void HideInstructions()
    {
        instructionsText.setVisibility(View.INVISIBLE);
        instructions_hidden = true;
    }

    /**
     * Gets accessibility standards from firebase database
     */
    private void UpdateDatabaseValues()
    {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Standards/Illuminance/Outdoor");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                outDoorParam = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference interiorHabDB = FirebaseDatabase.getInstance().getReference("Standards/Illuminance/Indoor");

        interiorHabDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                indoorWRampParam = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference minRangeDB = FirebaseDatabase.getInstance().getReference("Standards/Illuminance/minIndoorWR");

        minRangeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minIndoorH = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference maxRangeDB = FirebaseDatabase.getInstance().getReference("Standards/Illuminance/maxIndoorWR");

        maxRangeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxIndoorH = dataSnapshot.getValue(Float.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
