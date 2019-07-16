package com.example.admin.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.admin.administrador.adapter.Adapter;
import com.example.admin.administrador.javabean.Centro;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    List<Centro> centros;

    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        rv = (RecyclerView) findViewById( R.id.rvCentros );

        rv.setLayoutManager( new LinearLayoutManager( this ) );

        centros = new ArrayList<>();

        FirebaseDatabase databse = FirebaseDatabase.getInstance();

        adapter = new Adapter( centros );

        rv.setAdapter( adapter );

        adapter.notifyDataSetChanged();

        databse.getReference().child( "Usuarios" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                centros.removeAll( centros );
                for (DataSnapshot snapshost : dataSnapshot.getChildren()) {
                    Centro centro = snapshost.getValue( Centro.class );
                    centros.add( centro );
                }
                adapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
