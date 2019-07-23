package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class GuestActivity extends AppCompatActivity {
    ImageButton botonSalir;

    LinearLayout botonAseos;
    LinearLayout botonPuertas;

    LinearLayout botonIlum;
    LinearLayout botonAscensor;
    LinearLayout botonMostrador;
    LinearLayout botonRampa;
    LinearLayout botonSalvaescaleras;
    LinearLayout botonEstancias;
    LinearLayout botonPasillos;
    LinearLayout botonEmergencias;

    LinearLayout botonSimulacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        botonSalir = (ImageButton)findViewById(R.id.btnBack);


         botonPuertas= (LinearLayout)findViewById(R.id.BotonPuertas) ;
         botonIlum = (LinearLayout)findViewById(R.id.BotonIluminacion);
         botonAscensor= (LinearLayout)findViewById(R.id.BotonAscensores);
         botonMostrador= (LinearLayout)findViewById(R.id.BotonMostradores);
         botonRampa= (LinearLayout)findViewById(R.id.BotonRampas);
         botonSalvaescaleras= (LinearLayout)findViewById(R.id.BotonSalvaescaleras);

         botonEmergencias= (LinearLayout)findViewById(R.id.BotonEmergencias);

        botonSimulacion = (LinearLayout)findViewById(R.id.BotonSimulacion);


        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        botonPuertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.DOOR);
            }
        });
        botonIlum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.ILLUM);
            }
        });
        botonAscensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.ELEVATOR);
            }
        });
        botonMostrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo(MeasureCounter.class);}
        });
        botonRampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.RAMPS);
            }
        });
        botonSalvaescaleras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.STAIRLIFTER);
            }
        });

        botonEmergencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class,TypesManager.obsType.EMERGENCY);
            }
        });



        botonSimulacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(WheelchairSimulation.class);
            }
        });


    }


    public void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }

    void changeWindowTo(Class activity,TypesManager.obsType _type){
        Intent guestActivity = new Intent(this,activity);
        guestActivity.putExtra("obsType",_type.getValue());
        startActivity(guestActivity);
    }

}
