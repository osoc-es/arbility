package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button guestButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        guestButton = (Button)findViewById(R.id.BotonInvitado);

        guestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeWindowTo(GuestActivity.class);
            }
        });


    }

    void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }


}
