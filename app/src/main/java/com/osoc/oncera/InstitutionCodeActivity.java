package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.Institution;

public class InstitutionCodeActivity extends AppCompatActivity {

    TextView codigo;
    Button copiar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser usuario;

    private String emailPersona;
    private String codCentro;
    private ImageButton atras;

    private final Institution[] cen = new Institution[1];
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sesion_centro );

        mDatabaseRef = FirebaseDatabase.getInstance().getReference( "Users" );
        firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser();
        emailPersona = usuario.getEmail();
        System.out.println( emailPersona );


        codigo = (TextView) findViewById( R.id.tvCodCentro );
        copiar = (Button) findViewById( R.id.btnCopiarCod );
        atras = (ImageButton) findViewById(R.id.btnBack);

        loadCenterCode();

        copiar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = codigo.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text",  text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText( InstitutionCodeActivity.this, "Copiado en el Portapapeles", Toast.LENGTH_LONG ).show();


            }
        } );

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadCenterCode(){
        Query qq1 = mDatabaseRef.orderByChild("mail").equalTo(emailPersona);

        qq1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        cen[0] = dataSnapshot1.getValue(Institution.class);
                }

                if (emailPersona.equals( cen[0].getMail() )){

                    codCentro=cen[0].getCenterCode();

                    codigo.setText( codCentro );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
