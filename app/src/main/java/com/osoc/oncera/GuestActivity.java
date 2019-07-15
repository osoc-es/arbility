package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuestActivity extends AppCompatActivity {
    ImageButton botonSalir;

    LinearLayout botonAseos;
    LinearLayout botonPuertas;
    LinearLayout botonMostradores;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        botonSalir = (ImageButton)findViewById(R.id.BotonSalir);
        botonAseos = (LinearLayout)findViewById(R.id.BotonAseos);
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
