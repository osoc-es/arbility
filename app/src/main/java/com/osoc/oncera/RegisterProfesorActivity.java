package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.osoc.oncera.javabean.Institution;
import com.osoc.oncera.javabean.Teacher;

public class RegisterProfesorActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText etnombre;
    private EditText codCentro;
    private EditText etcorreo;
    private EditText etPassword, etPasswordRepeatProfesor;
    private Button btnRegistrar;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthProfe;
    private FirebaseUser user;

    private String password;
    private String password2;
    private String alias;
    private String mail;
    private String cod;

    private Teacher profe;

    private String codigo;

    private final Institution[] prf = new Institution[1];
    private final Teacher[] teacher = new Teacher[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_profesor );

        codCentro = (EditText) findViewById( R.id.etCodigoCentroProfesor );
        etnombre = (EditText) findViewById( R.id.etAlias );
        etcorreo = (EditText) findViewById( R.id.etEmailRegProfesor );

        etPassword = (EditText) findViewById( R.id.etPasswordRegProfesor );
        etPasswordRepeatProfesor = (EditText) findViewById( R.id.etPasswordRepeatProfesor );

        btnRegistrar = (Button) findViewById( R.id.btnRegistrar );


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Users" );

        progressDialog = new ProgressDialog( this );

        firebaseAuthProfe = FirebaseAuth.getInstance();

    }


    public void registrarProfesor() {

        mail = etcorreo.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeatProfesor.getText().toString().trim();
        alias = etnombre.getText().toString().trim();

        if (mail.isEmpty() || password.isEmpty()) {
            Toast.makeText( this, "Debe introducirse el email y la password", Toast.LENGTH_SHORT ).show();
        } else if (alias.isEmpty()) {
            Toast.makeText( this, "Debe introducir el name", Toast.LENGTH_SHORT ).show();
        } else if (password.length() < 6) {
            Toast.makeText( this, "La password debe contener al menos 6 caracteres", Toast.LENGTH_SHORT ).show();
        } else if (!password.equals( password2 )) {
            Toast.makeText( this, "Las contraseñas deben de coincidir", Toast.LENGTH_SHORT ).show();
        }
        progressDialog.setMessage( "Realizando registro en linea. . ." );
        progressDialog.show();

            firebaseAuthProfe.createUserWithEmailAndPassword( mail, password )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            user = firebaseAuthProfe.getCurrentUser();

                            String clave = user.getUid();

                            profe = new Teacher( clave, etnombre.getText().toString(), codCentro.getText().toString(), etcorreo.getText().toString().toLowerCase() );
                            mDatabaseRef.child( clave ).setValue( profe );

                            Intent i = new Intent( RegisterProfesorActivity.this, LoginProfesorActivity.class );
                            startActivity( i );
                        }
                    } );

        progressDialog.dismiss();

    }



    public void comprobarCodigo() {

        RegisterProfesorActivity.this.codigo = codCentro.getText().toString();
        Query qq2 = mDatabaseRef.orderByChild( "centerCode" ).equalTo( codigo ).limitToFirst( 1 );
        qq2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    prf[0] = dataSnapshot1.getValue( Institution.class );
                }

                if (prf[0] != null) {

                    if (prf[0].getCenterCode().equals( codigo ) && codigo != null) {
                        Toast.makeText( RegisterProfesorActivity.this, "Codigo correcto", Toast.LENGTH_LONG ).show();
                        registrarProfesor();
                    } else {
                        Toast.makeText( RegisterProfesorActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG ).show();
                    }

                } else {
                    Toast.makeText( RegisterProfesorActivity.this, "Prof null", Toast.LENGTH_LONG ).show();
                }

                qq2.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( RegisterProfesorActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );

    }
    public void comprobarAlias(View v){
        RegisterProfesorActivity.this.alias = etnombre.getText().toString().trim();
        Query qq4 = mDatabaseRef.orderByChild( "alias" ).equalTo( alias ).limitToFirst( 1 );
        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    teacher[0] = dataSnapshot1.getValue( Teacher.class );
                }

                if (teacher[0] != null) {

                    if (teacher[0].getAlias().equals( alias ) && alias != null) {
                        Toast.makeText( RegisterProfesorActivity.this, "Alias Repetido, por favor introduzca otro Alias", Toast.LENGTH_LONG ).show();

                    } else {
                        Toast.makeText( RegisterProfesorActivity.this, "Alias correcto", Toast.LENGTH_LONG ).show();
                        comprobarCodigo();
                    }

                } else {
                    Toast.makeText( RegisterProfesorActivity.this, "Codigo correcto", Toast.LENGTH_LONG ).show();
                    comprobarCodigo();

                }

                qq4.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( RegisterProfesorActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }
}
