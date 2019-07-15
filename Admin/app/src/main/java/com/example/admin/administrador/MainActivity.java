package com.example.admin.administrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.admin.administrador.adapter.Adaptador;
import com.example.admin.administrador.javabean.Centro;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvCentros;
    ArrayList<Centro> centro;
    Adaptador adaptador;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    LinearLayoutManager llManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        llManager= new LinearLayoutManager(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");

        rvCentros = findViewById(R.id.rvCentros);
        rvCentros.setHasFixedSize(true);

        adaptador = new Adaptador(getApplicationContext(), centro);
        rvCentros.setAdapter(adaptador);
    }
}
