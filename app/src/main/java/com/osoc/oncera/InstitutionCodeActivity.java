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

    TextView tvCode;
    Button btnCopy;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String email;
    private String centerCode;
    private ImageButton btnBack;

    private final Institution[] cen = new Institution[1];
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_center_session);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference( "Users" );
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        email = user.getEmail();
        System.out.println(email);


        tvCode = (TextView) findViewById( R.id.tvCenterCode);
        btnCopy = (Button) findViewById( R.id.btnCopyCod);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        loadCenterCode();

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCodeClipboard();
            }
        } );

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Copy the displayed center code to the clipboard
     */
    private void copyCodeClipboard(){
        String text = tvCode.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService( Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text",  text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText( InstitutionCodeActivity.this, "Copiado en el Portapapeles", Toast.LENGTH_LONG ).show();
    }

    /**
     * Get the center code and display it
     */
    private void loadCenterCode(){
        Query qq1 = mDatabaseRef.orderByChild("mail").equalTo(email);

        qq1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        cen[0] = dataSnapshot1.getValue(Institution.class);
                }

                if (email.equals( cen[0].getMail() )){

                    centerCode =cen[0].getCenterCode();

                    tvCode.setText(centerCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
