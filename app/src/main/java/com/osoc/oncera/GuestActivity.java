package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class GuestActivity extends AppCompatActivity {
    ImageButton btnBack;
    LinearLayout btnDoor;

    LinearLayout btnIlum;
    LinearLayout btnElevator;
    LinearLayout btnCounter;
    LinearLayout btnRamp;
    LinearLayout btnChairLift;
    LinearLayout btnEmergencies;

    LinearLayout btnSimulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        btnBack = (ImageButton)findViewById(R.id.btnBack);


        btnDoor = (LinearLayout)findViewById(R.id.btnDoor) ;
        btnIlum = (LinearLayout)findViewById(R.id.btnIlum);
        btnElevator = (LinearLayout)findViewById(R.id.btnElevator);
        btnCounter = (LinearLayout)findViewById(R.id.btnCounter);
        btnRamp = (LinearLayout)findViewById(R.id.btnRamp);
        btnChairLift = (LinearLayout)findViewById(R.id.btnChairLift);

        btnEmergencies = (LinearLayout)findViewById(R.id.btnEmergencies);

        btnSimulation = (LinearLayout)findViewById(R.id.BotonSimulacion);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btnDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.DOOR);
            }
        });
        btnIlum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.ILLUM);
            }
        });
        btnElevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.ELEVATOR);
            }
        });
        btnCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo(MeasureCounter.class);}
        });
        btnRamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.RAMPS);
            }
        });
        btnChairLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.STAIRLIFTER);
            }
        });

        btnEmergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.EMERGENCY);
            }
        });



        btnSimulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(WheelchairSimulation.class);
            }
        });


    }

    /**
     * Start the activity that is passed
     * @param activity
     */
    public void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }

    /**
     * Start LegalInfoActivity with the legal information corresponding to the obstacle type
     * @param activity activity to be started (LegalInfoActivty)
     * @param _type obstacle type
     */
    void changeWindowTo(Class activity,TypesManager.obsType _type){
        Intent guestActivity = new Intent(this,activity);
        guestActivity.putExtra("obsType",_type.getValue());
        startActivity(guestActivity);
    }

}
