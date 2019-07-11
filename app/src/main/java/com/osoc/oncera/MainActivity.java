package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button guestButton;
    private Button studentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guestButton = (Button)findViewById(R.id.BotonInvitado);

        guestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeWindowTo(GuestActivity.class);
            }
        });

        studentButton = (Button)findViewById(R.id.BotonAlumno);

        studentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeWindowTo(LegalInfoActivity.class);
            }
        });

    }

    void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }


}
