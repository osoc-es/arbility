package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnGuest;
    private Button btnTeacher;
    private Button btnCenter;
    private Button btnStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnGuest = (Button)findViewById(R.id.GuestButton);
        btnTeacher = (Button) findViewById( R.id.TeacherButton);
        btnStudent = (Button) findViewById( R.id.StudentButton);
        btnCenter = (Button)findViewById( R.id.RegisterCenterButton);

        btnGuest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeWindowTo(GuestActivity.class); }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeWindowTo(LoginStudentActivity.class); }
        });

        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo( LoginInstitutionActivity.class); }
        } );

        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changeWindowTo( LoginTeacherActivity.class); }
        } );
    }

    /**
     * Got to the inputted activity
     * @param activity activity that will be started
     */
    void changeWindowTo(Class activity){
        Intent guestActivity = new Intent(this,activity);
        startActivity(guestActivity);
    }


}
