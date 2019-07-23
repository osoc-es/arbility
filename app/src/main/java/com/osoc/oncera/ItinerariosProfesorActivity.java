package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.osoc.oncera.adapters.CardItinerariosAdapter;
import com.osoc.oncera.javabean.Itinerary;
import com.osoc.oncera.javabean.Teacher;

import java.util.ArrayList;

public class ItinerariosProfesorActivity extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<Itinerary> itinerarios;
    private final DatabaseReference mDatabaseRef =  FirebaseDatabase.getInstance().getReference("Users");
    CardItinerariosAdapter adapter;

   FirebaseUser firebaseUser;
   Button bntAniadir;
    ImageButton btnEl;
    String emailPersona;
    String codCentro;

    private final Teacher[] prf = new Teacher[1];


    //String codigoDelCentro;
 //   String aliasProfesor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_itinerarios_profesor );

        rv = findViewById( R.id.rvItinerario );
        rv.setLayoutManager( new LinearLayoutManager( this ) );

        itinerarios = new ArrayList<>();

        FirebaseDatabase databse = FirebaseDatabase.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailPersona = firebaseUser.getEmail();

        adapter = new CardItinerariosAdapter( itinerarios );

        rv.setAdapter( adapter );

        adapter.notifyDataSetChanged();

        obtenerProfesor();

        databse.getReference( "Itineraries" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itinerarios.removeAll( itinerarios );
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Itinerary iti = dataSnapshot1.getValue( Itinerary.class );

                   if(iti.getTeacherAlias().equals(prf[0].getAlias()) && iti.getCode().equals(prf[0].getCenterCode()))
                   {
                       itinerarios.add( iti );
                   }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        bntAniadir = findViewById( R.id.btnAniadir );
        btnEl = findViewById( R.id.btnEliminarItinerario );

        rv.setHasFixedSize( true );

        //cargarCodCentro();

        bntAniadir.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder( ItinerariosProfesorActivity.this );


                builder.setMessage( "¿Quieres hacer un nuevo itinerario? Se guardará automaticamente" )
                        .setPositiveButton( "Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO METODOS
                                Intent i = new Intent(ItinerariosProfesorActivity.this, CrearItinerarioActivity.class);
                                startActivity(i);
                            }
                        } ).setNegativeButton( "Cancelar", null );


                AlertDialog alert = builder.create();
                alert.show();

                v.setFocusable( false );

            }
        } );


    }

    public void obtenerProfesor(){

        Query qq4 = mDatabaseRef.orderByChild( "mail" ).equalTo( emailPersona );

        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    prf[0] = dataSnapshot1.getValue( Teacher.class );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( ItinerariosProfesorActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    /*private void cargarCodCentro() {
        Query qq = reference.orderByChild( "correo" ).equalTo( emailPersona );

        qq.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    prf[0] = dataSnapshot1.getValue( Teacher.class );
                    cargarItinerarios();
                }

                if (emailPersona.equals( prf[0].getMail() )) codCentro = prf[0].getCode();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );
    }*/
   /* private void cargarItinerarios() {
        reference = FirebaseDatabase.getInstance().getReference( "Itinerarios" );

        recyclerView.setLayoutManager( llManager );

        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Itinerary art;

                    try {
                        art = snapshot.getValue( Itinerary.class );

                        if (art.getCode().equals( codCentro )) {
                            lista.add( art );
                        }
                    } catch (DatabaseException de) {

                        break;
                    }
                    assert art != null;
                    assert firebaseUser != null;


                }

                for (Itinerary art : lista) {
                    System.out.println( art.getName() );
                }
                adapter = new CardItinerariosAdapter( lista );
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );*/
    }


