package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginInstitutionActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth auth;
    private ImageButton btnBack;
    private DatabaseReference mDatabaseRef;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        etEmail = (EditText) findViewById( R.id.etEmailLogin);
        etPassword = (EditText) findViewById( R.id.etPasswordLogin );
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Start the activity to register an institution
     * @param v
     */
    public void register(View v) {
            Intent i= new Intent( LoginInstitutionActivity.this, RegisterInstitutionActivity.class);
            startActivity(i);

            finish();
        }

    /**
     * Validate email and password and login if correct
      * @param v
     */
    public void login(View v){
        email = etEmail.getText().toString();
        final String inputPasword = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Introduce el Email, por favor", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(inputPasword)) {
            Toast.makeText(getApplicationContext(), "Introduce la Contraseña, por favor", Toast.LENGTH_LONG).show();
            return;
        }

        //AUTENTIFICACION
        auth.signInWithEmailAndPassword(email, inputPasword)
                .addOnCompleteListener(LoginInstitutionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent( LoginInstitutionActivity.this, InstitutionCodeActivity.class );
                            startActivity( i );
                            finish();

                        } else {
                            Toast.makeText( LoginInstitutionActivity.this, "El email o la contraseña no es correcta", Toast.LENGTH_LONG ).show();
                        }
                    }
                });

    }
}
