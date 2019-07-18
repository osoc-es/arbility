package com.osoc.oncera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.osoc.oncera.adapters.CardItinerariosAdapter;
import com.osoc.oncera.javabean.Itinerario;

import java.util.ArrayList;

public class ItinerariosProfesorActivity extends AppCompatActivity {

    private ArrayList<Itinerario> lista;

    private RecyclerView recyclerView;
    private CardItinerariosAdapter adapter;
    private LinearLayout llManager;

    FirebaseUser firebaseUser;
    private DatabaseReference reference;


    String emailPersona;
    String codCentro;

    ArrayList<String> listaIti;
    ImageButton btnEl;
    FloatingActionButton fam;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_itinerarios_profesor );
    }
}
