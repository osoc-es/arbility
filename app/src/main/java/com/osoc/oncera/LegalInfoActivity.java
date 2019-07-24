package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LegalInfoActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView desc;
    Button btnEvaluate;
    String descText;
    TypesManager.obsType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_info);

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnEvaluate = (Button)findViewById(R.id.btnEvaluate);
        desc = (TextView)findViewById(R.id.descText);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        int typeID = getIntent().getIntExtra("obsType",-1);

        type = TypesManager.obsType.valueOf(typeID);
        selectText();


        btnEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type == TypesManager.obsType.DOOR)  changeWindowTo(MeasureDoor.class);
                else if (type == TypesManager.obsType.ILLUM) changeWindowTo(LuxMeter.class);
                else if (type == TypesManager.obsType.ELEVATOR) changeWindowTo(MeasureElevator.class);
                else if (type == TypesManager.obsType.ATTPOINT) changeWindowTo(MeasureCounter.class);
                else if (type == TypesManager.obsType.RAMPS) changeWindowTo(MeasureRamp.class);
                else if (type == TypesManager.obsType.STAIRLIFTER) changeWindowTo(MeasureStairLift.class);
                else if (type == TypesManager.obsType.EMERGENCY) changeWindowTo(MeasureEmergencies.class);
            }
        });
    }

    /**
     * Select legal info text depending on the obstacle type
     */
    private void selectText(){

        if(type == TypesManager.obsType.ASEOS) descText = getString(R.string.descAseos);
        else if (type == TypesManager.obsType.DOOR)  descText = getString(R.string.descPuertas);
        else if (type == TypesManager.obsType.ILLUM)  descText = getString(R.string.descIlum);
        else if (type == TypesManager.obsType.ELEVATOR)  descText = getString(R.string.descAscensor);
        else if (type == TypesManager.obsType.ATTPOINT)  descText = getString(R.string.descMostrador);
        else if (type == TypesManager.obsType.RAMPS)  descText = getString(R.string.descRampa);
        else if (type == TypesManager.obsType.STAIRLIFTER)  descText = getString(R.string.descSalvaescaleras);
        else if (type == TypesManager.obsType.EMERGENCY)  descText = getString(R.string.descEmergencias);


        desc.setText(descText);

    }

    /**
     * Change window to the tool to evaluate the accessibility of the obstacle
     * @param activity the activity that will be started
     */
    public void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }
}
