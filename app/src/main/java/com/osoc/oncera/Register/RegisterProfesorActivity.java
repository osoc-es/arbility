package com.osoc.oncera.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.osoc.oncera.LogIn.LoginProfesorActivity;
import com.osoc.oncera.R;
import com.osoc.oncera.javabean.Centro;
import com.osoc.oncera.javabean.Profesor;

public class RegisterProfesorActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private EditText nombre;
    private EditText codCentro;
    private EditText correo;
    private EditText etPassword, etPasswordRepeat;
    private Button btnRegistrar;

    private Uri mImageUri;
    private StorageReference mStorageRefInq;
    private StorageReference mStorageRefCas;
    private DatabaseReference mDatabaseRef;
    private ChildEventListener cel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String password;
    private String password2;
    private String alias;
    private String mail;
    private String cod;

    private Profesor profe;

    private UploadTask ut;
    private Boolean imagenSubida = false;


    private  String codigo;



    private final Profesor[] prf = new Profesor[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_profesor );

        codCentro = (EditText) findViewById( R.id.etCodigoCentro );
        nombre = (EditText) findViewById( R.id.etAlias );
        correo = (EditText) findViewById( R.id.etCorreoLogin );

        btnRegistrar = (Button) findViewById( R.id.btnRegistrar );

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRefInq = FirebaseStorage.getInstance().getReference("FotosCentro");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Usuarios");
    }







    public void registrar(View v) {

        comprobarCodigo();
        String warning = validarDatos();


        if (warning == null) {
            progressDialog.setMessage( "Realizando registro en linea. . ." );
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Usuarios");

                                user = firebaseAuth.getCurrentUser();


                                String clave = user.getUid();
                                if (imagenSubida) {
                                    Uri selectedUri = mImageUri;

                                    StorageReference fotoRef = mStorageRefCas.child(selectedUri.getLastPathSegment());
                                    ut = fotoRef.putFile(selectedUri);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {

                                            ut.addOnSuccessListener( RegisterProfesorActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            profe = new Profesor(clave, nombre.getText().toString(),codCentro.getText().toString(), correo.getText().toString().toLowerCase());
                                                            mDatabaseRef.child(clave).setValue(profe);

                                                            Intent i = new Intent(RegisterProfesorActivity.this, LoginProfesorActivity.class);
                                                            startActivity(i);


                                                            Query qq=mDatabaseRef.orderByChild("emailUsuario").equalTo(mail);



                                                            qq.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                            mDatabaseRef.child("codCentro").setValue(codCentro);


                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }, 5000);

                                } else {

                                    profe = new Profesor(clave, nombre.getText().toString(),codCentro.getText().toString(), correo.getText().toString().toLowerCase());
                                    mDatabaseRef.child(clave).setValue(profe);
                                    Toast.makeText( RegisterProfesorActivity.this, "Registrado Correcto, Valídalo para porder Loguearte", Toast.LENGTH_LONG ).show();

                                   Intent i = new Intent(RegisterProfesorActivity.this, LoginProfesorActivity.class);
                                    startActivity(i);

                                }

                            } else {
                                Toast.makeText( RegisterProfesorActivity.this, getString(R.string.msj_no_registrado), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        } else {
            Toast.makeText(this, warning,
                    Toast.LENGTH_LONG).show();
            progressDialog.dismiss();



        }
    }

    private String validarDatos() {

        String msj = null;

        mail = correo.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeat.getText().toString().trim();
        alias = nombre.getText().toString().trim();
        cod = codCentro.getText().toString().trim();

        if (mail.isEmpty() || password.isEmpty()) {
            msj = "Debe introducirse el email y la password";
        }else if(alias.isEmpty()) {
            msj = "Debe introducir el nombre";
        }else if (password.length() < 6) {
            msj = "La password debe contener al menos 6 caracteres";
        } else if (!password.equals(password2)) {
            msj = "Las contraseñas deben de coincidir";
        }

        return msj;
    }
    public void comprobarCodigo() {

        codigo = codCentro.getText().toString();


        if (codigo.isEmpty()) {
            Toast.makeText(this, "Debes introducir un codigo", Toast.LENGTH_LONG).show();


        } else {


            Query qq = mDatabaseRef.orderByChild("codCentro").equalTo(codigo).limitToFirst(1);

            qq.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        prf[0] = dataSnapshot1.getValue(Profesor.class);
                    }

                    if (prf[0] != null) {

                        if(prf[0].getCodCentro().equals(codigo)){
                            Toast.makeText(RegisterProfesorActivity.this, "Codigo correcto", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegisterProfesorActivity.this, LoginProfesorActivity.class);
                            startActivity(i);

                        }else{

                            Toast.makeText(RegisterProfesorActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG).show();

                        }


                    } else {
                        Toast.makeText(RegisterProfesorActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG).show();


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(RegisterProfesorActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }
}
