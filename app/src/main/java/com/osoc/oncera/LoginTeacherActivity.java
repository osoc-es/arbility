package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginTeacherActivity extends AppCompatActivity {

    private EditText correo;
    private EditText password;
    private FirebaseAuth auth;
    private ImageButton atras;
    private DatabaseReference mDatabaseRef;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        correo = (EditText) findViewById( R.id.etCorreoLogin );
        password = (EditText) findViewById( R.id.etPasswordLogin );
        atras = (ImageButton) findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void register(View v) {


        Intent i= new Intent( LoginTeacherActivity.this, RegisterTeacherActivity.class);
        startActivity(i);

    }

    public void login(View v){

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
                .addOnCompleteListener(LoginTeacherActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent( LoginTeacherActivity.this, TeacherItineraryList.class );
                            startActivity( i );

                        } else {
                            Toast.makeText( LoginTeacherActivity.this, "El email o la contraseña no es correcta", Toast.LENGTH_LONG ).show();
                        }
                    }
                });

    }
}
