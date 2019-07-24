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

public class RegisterTeacherActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText etname;
    private EditText centerCode;
    private EditText etemail;
    private EditText etPassword, etPasswordRepeatTeacher;
    private Button btnRegister;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuthProfe;
    private FirebaseUser user;

    private String password;
    private String password2;
    private String alias;
    private String mail;

    private Teacher profe;

    private String codigo;

    private final Institution[] prf = new Institution[1];
    private final Teacher[] teacher = new Teacher[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_teacher);

        centerCode = (EditText) findViewById( R.id.etCenterCodeTeacher);
        etname = (EditText) findViewById( R.id.etAlias );
        etemail = (EditText) findViewById( R.id.etEmailRegProfesor );

        etPassword = (EditText) findViewById( R.id.etPasswordRegProfesor );
        etPasswordRepeatTeacher = (EditText) findViewById( R.id.etPasswordRepeatTeacher);

        btnRegister = (Button) findViewById( R.id.btnRegister);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Users" );

        progressDialog = new ProgressDialog( this );

        firebaseAuthProfe = FirebaseAuth.getInstance();

    }

    /**
     * Save new Teacher in the database or generate an error message
     */
    private void registerTeacher() {

        mail = etemail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeatTeacher.getText().toString().trim();
        alias = etname.getText().toString().trim();

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

                            profe = new Teacher( clave, etname.getText().toString(), centerCode.getText().toString(), etemail.getText().toString().toLowerCase() );
                            mDatabaseRef.child( clave ).setValue( profe );

                            Intent i = new Intent( RegisterTeacherActivity.this, LoginTeacherActivity.class );
                            startActivity( i );
                        }
                    } );

        progressDialog.dismiss();

    }


    /**
     * Check whether the code written by the teacher matches the one of any center
     */
    private void checkCenterCode() {
        RegisterTeacherActivity.this.codigo = centerCode.getText().toString();
        Query qq2 = mDatabaseRef.orderByChild( "centerCode" ).equalTo( codigo ).limitToFirst( 1 );
        qq2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    prf[0] = dataSnapshot1.getValue( Institution.class );
                }

                if (prf[0] != null) {

                    if (prf[0].getCenterCode().equals( codigo ) && codigo != null) {
                        Toast.makeText( RegisterTeacherActivity.this, "Codigo correcto", Toast.LENGTH_LONG ).show();
                        registerTeacher();
                    } else {
                        Toast.makeText( RegisterTeacherActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG ).show();
                    }

                } else {
                    Toast.makeText( RegisterTeacherActivity.this, "Prof null", Toast.LENGTH_LONG ).show();
                }

                qq2.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( RegisterTeacherActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );

    }

    /**
     * Function to check that the users alias is unique, creating a code if it is and displaying an
     * error on the contrary
     * @param v
     */
    private void checkUniqueAlias(View v){
        RegisterTeacherActivity.this.alias = etname.getText().toString().trim();
        Query qq4 = mDatabaseRef.orderByChild( "alias" ).equalTo( alias ).limitToFirst( 1 );
        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    teacher[0] = dataSnapshot1.getValue( Teacher.class );
                }

                if (teacher[0] != null) {

                    if (teacher[0].getAlias().equals( alias ) && alias != null) {
                        Toast.makeText( RegisterTeacherActivity.this, "Alias Repetido, por favor introduzca otro Alias", Toast.LENGTH_LONG ).show();

                    } else {
                        Toast.makeText( RegisterTeacherActivity.this, "Alias correcto", Toast.LENGTH_LONG ).show();
                        checkCenterCode();
                    }

                } else {
                    Toast.makeText( RegisterTeacherActivity.this, "Codigo correcto", Toast.LENGTH_LONG ).show();
                    checkCenterCode();

                }

                qq4.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( RegisterTeacherActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }
}
