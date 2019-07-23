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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.adapters.CardItinerariosAdapter;
import com.osoc.oncera.javabean.Itinerary;
import com.osoc.oncera.javabean.Teacher;

import java.util.ArrayList;

public class ItinerariosProfesorActivity extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<Itinerary> itinerarios;

    CardItinerariosAdapter adapter;
   /* ArrayList<Itinerario> lista;
    private DatabaseReference mDatabaseRef;
    RecyclerView rv;
    Adapter adapter;
    private LinearLayoutManager llManager;


    private DatabaseReference reference;

    private final Teacher[] prf = new Teacher[1];



    ArrayList<String> listaIti;


    //FloatingActionButton fam;*/
   FirebaseUser firebaseUser;
   Button bntAniadir;
    ImageButton btnEl;
    String emailPersona;
    String codCentro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_itinerarios_profesor );

        rv = findViewById( R.id.rvItinerario );
        rv.setLayoutManager( new LinearLayoutManager( this ) );

        itinerarios = new ArrayList<>();

        FirebaseDatabase databse = FirebaseDatabase.getInstance();

        adapter = new CardItinerariosAdapter( itinerarios );

        rv.setAdapter( adapter );

        adapter.notifyDataSetChanged();

        databse.getReference().child( "Itineraries" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itinerarios.removeAll( itinerarios );
                for (DataSnapshot snapshost : dataSnapshot.getChildren()) {
                    Itinerary iti = snapshost.getValue( Itinerary.class );
                    itinerarios.add( iti );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        /*btnEl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder( ItinerariosProfesorActivity.this );
                    builder.setMessage( "¿Seguro qué quieres eliminar este itinerario de la lista?" )
                            .setPositiveButton( "Si", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String seleccionado;
                                    Itinerario iti = new Itinerario(  );
                                    iti.setId( seleccionado.getId );
                                    mLista.remove( mLista.get( position ).getId() );
                                    reference.child( mLista.get( position ).getId() ).removeValue();
                                    removeAt( position );
                                }
                            } ).setNegativeButton( "Cancelar", null );

                    AlertDialog alert = builder.create();
                    alert.show();

            }
        } );*/

        bntAniadir = findViewById( R.id.btnAniadir );
        btnEl = findViewById( R.id.btnEliminarItinerario );



        rv.setHasFixedSize( true );



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailPersona = firebaseUser.getEmail();

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


