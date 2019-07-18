package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button guestButton;
    private Button btnProfesor;
    private Button btnCentro;
    private Button btnAlumno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        guestButton = (Button)findViewById(R.id.BotonInvitado);
        btnProfesor = (Button) findViewById( R.id.BotonProfesor );
        btnAlumno = (Button) findViewById( R.id.BotonAlumno );
        btnCentro = (Button)findViewById( R.id.BotonRegistrarColegio );

        guestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeWindowTo(GuestActivity.class); }
        });

        btnAlumno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeWindowTo(LoginAlumnoActivity.class); }
        });

        btnCentro.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo( LoginCentroEscolarActivity.class); }
        } );

        btnProfesor.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo( LoginProfesorActivity.class); }
        } );


    }

    void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }


}
