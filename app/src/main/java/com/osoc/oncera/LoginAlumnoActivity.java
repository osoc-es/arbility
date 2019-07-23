package com.osoc.oncera;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.osoc.oncera.javabean.Itinerario;

public class LoginAlumnoActivity extends AppCompatActivity {

    private EditText etCodigoItinerario;
    private Button btnItineratio;
    private ImageButton atras;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthProfe;
    private FirebaseUser user;

    private String itinerario;


    private final Itinerario[] iti = new Itinerario[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_alumno );

        etCodigoItinerario = (EditText) findViewById( R.id.etCodigoItinerario );

        etCodigoItinerario.setInputType(InputType.TYPE_CLASS_TEXT);

        btnItineratio = (Button) findViewById( R.id.btnItinerario );
        atras = (ImageButton) findViewById(R.id.btnBack);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Itinerarios" );


        firebaseAuthProfe = FirebaseAuth.getInstance();


        btnItineratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerCodigoItinerario();



            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void obtenerCodigoItinerario(){
        itinerario = etCodigoItinerario.getText().toString().trim();
        Query qq2 = mDatabaseRef.orderByChild( "codItinerario" ).equalTo( itinerario ).limitToFirst( 1 );
        qq2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    iti[0] = dataSnapshot1.getValue( Itinerario.class );
                }

                if (iti[0] != null) {

                    if (iti[0].getCodItinerario().equals( itinerario ) && itinerario != null) {
                        Toast.makeText( LoginAlumnoActivity.this, "REALIZANDO ACTIVITY PARA DESCARGAR ITINERARIOS", Toast.LENGTH_LONG ).show();
                        /*Intent i = new Intent(LoginAlumnoActivity.this, MapaItinerarioActivity.class);
                        i.putExtra( "codigoItinerario", itinerario );
                        startActivity(i);*/
                    } else {
                        Toast.makeText( LoginAlumnoActivity.this, "El Código no Existe", Toast.LENGTH_LONG ).show();
                    }

                } else {
                    Toast.makeText( LoginAlumnoActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG ).show();
                }

                qq2.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( LoginAlumnoActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

}
