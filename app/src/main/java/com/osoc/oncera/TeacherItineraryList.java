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

public class TeacherItineraryList extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<Itinerary> itineraries;
    private  DatabaseReference mDatabaseRef;
    CardItinerariosAdapter adapter;
    FirebaseUser firebaseUser;
    Button bntAdd;
    ImageButton btnDel;
    String emailPerson;

    private final Teacher[] prf = new Teacher[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_itinerarios_profesor );

        rv = findViewById( R.id.rvItinerary);
        rv.setLayoutManager( new LinearLayoutManager( this ) );

        itineraries = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailPerson = firebaseUser.getEmail();

        adapter = new CardItinerariosAdapter(itineraries);

        rv.setAdapter( adapter );

        adapter.notifyDataSetChanged();

        obtenerProfesor();


        mDatabaseRef =  FirebaseDatabase.getInstance().getReference("Itineraries");

        Query qq = mDatabaseRef;

        qq.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                itineraries.removeAll(itineraries);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Itinerary iti = dataSnapshot1.getValue( Itinerary.class );

                   if(iti.getTeacherAlias().equals(prf[0].getAlias()) && iti.getCode().equals(prf[0].getCenterCode()))
                   {
                       itineraries.add( iti );
                   }

                }
                adapter.notifyDataSetChanged();

                qq.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        bntAdd = findViewById( R.id.btnAddItinerary);
        btnDel = findViewById( R.id.btnDeleteItinerary);

        rv.setHasFixedSize( true );

        //loadCenterCode();

        bntAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItineraryDialog();
            }
        } );
    }

    /**
     * Generate a dialog to ask the user whether they want to create a new itinerary or not
     */
    private void newItineraryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder( TeacherItineraryList.this );

        builder.setMessage( "¿Quieres hacer un nuevo itinerario?" )
                .setPositiveButton( "Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO METODOS
                        Intent i = new Intent(TeacherItineraryList.this, CreateItineraryActivity.class);
                        startActivity(i);
                    }
                } ).setNegativeButton( "Cancelar", null );


        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * get Teacher object with the mail of the logged user
     */
    private void obtenerProfesor()
    {
        mDatabaseRef =  FirebaseDatabase.getInstance().getReference("Users");

        Query qq4 = mDatabaseRef.orderByChild( "mail" ).equalTo(emailPerson);

        qq4.addListenerForSingleValueEvent( new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    prf[0] = dataSnapshot1.getValue( Teacher.class );
                }

                qq4.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( TeacherItineraryList.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );

    }
}


