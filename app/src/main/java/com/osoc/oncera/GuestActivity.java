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
    LinearLayout botonMostradores;

    LinearLayout botonIluminacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        botonSalir = (ImageButton)findViewById(R.id.BotonSalir);
        botonAseos = (LinearLayout)findViewById(R.id.BotonAseos);
        botonSimulacion = (LinearLayout)findViewById(R.id.BotonSimulacion);
        botonIluminacion = (LinearLayout) findViewById(R.id.BotonIluminacion);
        botonPuertas = (LinearLayout)findViewById(R.id.BotonPuertas);
        botonMostradores = (LinearLayout)findViewById(R.id.BotonMostradores);

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        botonAseos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(LegalInfoActivity.class);
            }
        });

        botonPuertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(MeasureActivity.class);
            }
        });

        botonIluminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeWindowTo(Luxometro.class);
            }

        botonMostradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo(MedirMostradorActivity.class);}
        });

    }

    void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }

}
