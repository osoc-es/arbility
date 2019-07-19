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
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.adapters.CardItinerariosAdapter;
import com.osoc.oncera.javabean.Itinerario;
import com.osoc.oncera.javabean.Profesor;

import java.util.ArrayList;

public class ItinerariosProfesorActivity extends AppCompatActivity {

    private ArrayList<Itinerario> lista;

    private RecyclerView recyclerView;
    private CardItinerariosAdapter adapter;
    private LinearLayoutManager llManager;

    FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private final Profesor[] prf = new Profesor[1];


    String emailPersona;
    String codCentro;

    ArrayList<String> listaIti;
    ImageButton btnEl;
    Button bntAniadir;
    //FloatingActionButton fam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_itinerarios_profesor );





       // final FloatingActionButton fab2 = (FloatingActionButton) findViewById( R.id.fab2 );


        btnEl = findViewById( R.id.btnEliminarIti );
        bntAniadir = findViewById( R.id.btnAniadir );

        llManager = new LinearLayoutManager( this );

        recyclerView = findViewById( R.id.rvItinerario );
        recyclerView.setHasFixedSize( true );

        lista = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailPersona = firebaseUser.getEmail();
        reference = FirebaseDatabase.getInstance().getReference( "Usuarios" );

        cargarCodCentro();

        bntAniadir.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder( ItinerariosProfesorActivity.this );


                builder.setMessage( "¿Quieres hacer un nuevo itinerario? Se guardará automaticamente" )
                        .setPositiveButton( "Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO METODOS
                                Intent i = new Intent(ItinerariosProfesorActivity.this, crearItinerarioActivity.class);
                                startActivity(i);
                            }
                        } ).setNegativeButton( "Cancelar", null );


                AlertDialog alert = builder.create();
                alert.show();

                v.setFocusable( false );

            }
        } );

    }

    private void cargarCodCentro() {
        Query qq = reference.orderByChild( "correo" ).equalTo( emailPersona );

        qq.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    prf[0] = dataSnapshot1.getValue( Profesor.class );
                    cargarItinerarios();
                }

                if (emailPersona.equals( prf[0].getCorreo() )) codCentro = prf[0].getCodCentro();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );
    }
    private void cargarItinerarios() {
        reference = FirebaseDatabase.getInstance().getReference( "Itinerarios" );

        recyclerView.setLayoutManager( llManager );

        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Itinerario art;

                    try {
                        art = snapshot.getValue( Itinerario.class );

                        if (art.getCodCentro().equals( codCentro )) {
                            lista.add( art );
                        }
                    } catch (DatabaseException de) {

                        break;
                    }
                    assert art != null;
                    assert firebaseUser != null;


                }

                for (Itinerario art : lista) {
                    System.out.println( art.getNombre() );
                }
                adapter = new CardItinerariosAdapter( lista );
                recyclerView.setAdapter( adapter );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}

