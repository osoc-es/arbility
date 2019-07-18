package com.osoc.oncera;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.osoc.oncera.javabean.Centro;
import com.osoc.oncera.javabean.Profesor;

public class LoginAlumnoActivity extends AppCompatActivity {

    private EditText etCodigoItinerario;
    private Button btnItineratio;
    private ImageButton atras;

    /*private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthProfe;
    private FirebaseUser user;*/

    private String codigo = "patata";

    private final Centro[] prf = new Centro[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_alumno );

        etCodigoItinerario = (EditText) findViewById( R.id.etCodigoItinerario );

        btnItineratio = (Button) findViewById( R.id.btnItinerario );
        atras = (ImageButton) findViewById(R.id.btnAtras);


        btnItineratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etCodigoItinerario.getText().toString().equals(codigo)){
                    Toast.makeText(LoginAlumnoActivity.this, "Correcto", Toast.LENGTH_SHORT).show();
                    Intent guestActivity = new Intent(LoginAlumnoActivity.this, MapaItinerarioActivity.class);
                    startActivity(guestActivity);
                }
                else
                    Toast.makeText(LoginAlumnoActivity.this, "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Usuarios" );

        progressDialog = new ProgressDialog( this );

        firebaseAuthProfe = FirebaseAuth.getInstance();*/

    }

}
