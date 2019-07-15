package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginCentroEscolarActivity extends AppCompatActivity {
    private EditText correo;
    private EditText password;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        correo = (EditText) findViewById( R.id.etCorreoLogin );
        password = (EditText) findViewById( R.id.etPasswordLogin );

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Usuarios");
    }
    public void register(View v) {


            Intent i= new Intent( LoginCentroEscolarActivity.this, RegisterCentroEscolarActivity.class);
            startActivity(i);

        }
}
