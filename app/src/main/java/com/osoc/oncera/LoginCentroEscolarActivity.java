package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginCentroEscolarActivity extends AppCompatActivity {
    private EditText correo;
    private EditText password;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;

    private String email;

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

    public void loguearse(View v){

        email = correo.getText().toString();
        final String contrasenia = password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Introduce el Email, por favor", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(contrasenia)) {
            Toast.makeText(getApplicationContext(), "Introduce la Contraseña, por favor", Toast.LENGTH_LONG).show();
            return;
        }

        //AUTENTIFICACION



        auth.signInWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(LoginCentroEscolarActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent( LoginCentroEscolarActivity.this, SesionCentroActivity.class );
                            startActivity( i );

                        } else {
                            Toast.makeText( LoginCentroEscolarActivity.this, "El email o la contraseña no es correcta", Toast.LENGTH_LONG ).show();
                        }
                    }
                });

    }
}
