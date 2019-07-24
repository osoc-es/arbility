package com.osoc.oncera;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.osoc.oncera.javabean.Itinerary;

public class LoginStudentActivity extends AppCompatActivity {

    private EditText etItineraryCode;
    private Button btnItinerary;
    private ImageButton btnBack;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthTeacher;
    private FirebaseUser user;

    private String itinerary;


    private final Itinerary[] iti = new Itinerary[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_student);

        etItineraryCode = (EditText) findViewById( R.id.etCodigoItinerario );

        etItineraryCode.setInputType(InputType.TYPE_CLASS_TEXT);

        btnItinerary = (Button) findViewById( R.id.btnItinerary);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Itineraries" );


        firebaseAuthTeacher = FirebaseAuth.getInstance();


        btnItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItineraryCode();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Check if the itinerary exists. If it does the student will be redirected to the map activity
     * otherwise an error message will be displayed
     */
    public void getItineraryCode(){
        itinerary = etItineraryCode.getText().toString().trim();
        Query qq2 = mDatabaseRef.orderByChild( "itineraryCode" ).equalTo(itinerary).limitToFirst( 1 );
        qq2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    iti[0] = dataSnapshot1.getValue( Itinerary.class );
                }

                if (iti[0] != null) {

                    if (iti[0].getItineraryCode().equals(itinerary) && itinerary != null) {
                        //Toast.makeText( LoginStudentActivity.this, "REALIZANDO ACTIVITY PARA DESCARGAR ITINERARIOS", Toast.LENGTH_LONG ).show();
                        Intent i = new Intent(LoginStudentActivity.this, ItineraryMapActivity.class);
                        i.putExtra( "itineraryCode", itinerary);
                        startActivity(i);
                    } else {
                        Toast.makeText( LoginStudentActivity.this, "El Código no Existe", Toast.LENGTH_LONG ).show();
                    }

                } else {
                    Toast.makeText( LoginStudentActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG ).show();
                }

                qq2.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( LoginStudentActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

}
