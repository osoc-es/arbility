package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.osoc.oncera.javabean.Institution;

public class RegisterCentroEscolarActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etCiudad;
    private EditText etEmail;
    private EditText etDireccion;
    private EditText etPassword, etPasswordRepeat;
    private ProgressDialog progressDialog;
    private Button btnRegistrar;
    private Button btnValidar;


    private DatabaseReference mDatabaseRef;
    public static FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String email;

    private String password;
    private String password2;
    private String nombre;
    private String ciudad;
    private String direccion;
    private Boolean validarCentro;

    private String correo;


    private Institution institution;

    private String codCentro;

    private char[] conjunto = new char[6];
    char[] elementos={'0','1','2','3','4','5','6','7','8','9' ,'a',
            'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    String codigoCentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        validarCentro = false;
        etNombre = (EditText) findViewById(R.id.etNombreReg);
        etCiudad = (EditText) findViewById(R.id.etCiudadReg);
        etDireccion = (EditText) findViewById( R.id.etDireccion );
        etEmail = (EditText) findViewById(R.id.etEmailReg);
        etPassword = (EditText) findViewById(R.id.etPasswordReg);
        etPasswordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        btnRegistrar = (Button) findViewById( R.id.btnRegistrarCentro);
        btnValidar = (Button) findViewById( R.id.btnValidar );




        //progressDialog = new ProgressDialog(this);
        correo = "danisom1b@gmail.com";
    }
    public void registrar(View v) {
        String warning = validarDatos();


        if (warning == null) {
            //progressDialog.setMessage( "Realizando registro en linea. . ." );
            //  progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                codCentro=crearcodCentro();
                                mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Users");

                                user = firebaseAuth.getCurrentUser();


                                String clave = user.getUid();

                                    institution = new Institution(clave, codCentro,  etNombre.getText().toString(), etEmail.getText().toString().toLowerCase(),etCiudad.getText().toString(), etDireccion.getText().toString(),validarCentro);
                                    mDatabaseRef.child(user.getUid()).setValue(institution);
                                    Toast.makeText( RegisterCentroEscolarActivity.this, "Registrado Correcto, Valídalo para porder Loguearte", Toast.LENGTH_LONG ).show();

                                    btnRegistrar.setVisibility(View.INVISIBLE);


                            } else {
                                Toast.makeText( RegisterCentroEscolarActivity.this, getString(R.string.msj_no_registrado), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        } else {
            Toast.makeText(this, warning,
                    Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();



        }

    }
    public void validar(View v){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse( "mailto:" + correo ) );
        i.putExtra(Intent.EXTRA_SUBJECT,etNombre.getText().toString());
        i.putExtra(Intent.EXTRA_TEXT,etDireccion.getText().toString());
        startActivity( i );
    }

    private String validarDatos() {

        String msj = null;

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeat.getText().toString().trim();
        nombre = etNombre.getText().toString().trim();
        ciudad = etCiudad.getText().toString().trim();
        direccion = etDireccion.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            msj = "Debe introducirse el email y la password";
        }else if (nombre.isEmpty() || ciudad.isEmpty() || direccion.isEmpty()) {
            msj = "Debe introducir el name, la ciudad y la direccion";
        }else if (password.length() < 6) {
            msj = "La password debe contener al menos 6 caracteres";
        } else if (!password.equals(password2)) {
            msj = "Las contraseñas deben de coincidir";
        }

        return msj;
    }

    public String crearcodCentro(){

        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            conjunto[i] = (char)elementos[el];
        }
        return codigoCentro = new String(conjunto);
    }
}
