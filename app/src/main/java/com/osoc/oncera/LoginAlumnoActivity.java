package com.osoc.oncera;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.osoc.oncera.javabean.Institution;

public class LoginAlumnoActivity extends AppCompatActivity {

    private EditText etCodigoItinerario;
    private Button btnItineratio;
    private ImageButton atras;

    /*private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthProfe;
    private FirebaseUser user;*/

    private String codigo = "patata";

    private final Institution[] prf = new Institution[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_alumno );

        etCodigoItinerario = (EditText) findViewById( R.id.etCodigoItinerario );

        etCodigoItinerario.setInputType(InputType.TYPE_CLASS_TEXT);

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
