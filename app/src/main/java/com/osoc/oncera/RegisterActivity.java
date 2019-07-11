package com.osoc.oncera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.BoringLayout;
import android.view.View;
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
import com.osoc.oncera.javabean.Centro;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etCiudad;
    private EditText etEmail;
    private EditText etDireccion;
    private EditText etPassword, etPasswordRepeat;
    private ProgressDialog progressDialog;

    private Uri mImageUri;
    private StorageReference mStorageRefInq;
    private StorageReference mStorageRefCas;
    private DatabaseReference mDatabaseRef;
    private ChildEventListener cel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String email;

    private String password;
    private String password2;
    private String nombre;
    private String ciudad;
    private String direccion;
    private Boolean validar = false;

    private String correo;


    private Centro centro;

    private UploadTask ut;
    private Boolean imagenSubida = false;

    private String codCasa;

    private char[] conjunto = new char[6];
    char[] elementos={'0','1','2','3','4','5','6','7','8','9' ,'a',
            'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    String codigoCasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRefInq = FirebaseStorage.getInstance().getReference("FotosCentro");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        etNombre = (EditText) findViewById(R.id.etNombreReg);
        etCiudad = (EditText) findViewById(R.id.etCiudadReg);
        etDireccion = (EditText) findViewById( R.id.etDireccion );
        etEmail = (EditText) findViewById(R.id.etEmailReg);
        etPassword = (EditText) findViewById(R.id.etPasswordReg);
        etPasswordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);




        progressDialog = new ProgressDialog(this);
        correo = "danisom1b@gmail.com";
    }


public void validar(View v){
    Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse( "mailto:" + correo ) );
    i.putExtra(Intent.EXTRA_SUBJECT,etNombre.getText().toString());
    i.putExtra(Intent.EXTRA_TEXT,etDireccion.getText().toString());
    startActivity( i );
}
    public void registrar(View v) {
        String warning = validarDatos();


        if (warning == null) {
            progressDialog.setMessage( "Realizando registro en linea. . ." );
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                codCasa=crearCodCasa();


                                mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Usuarios");

                                user = firebaseAuth.getCurrentUser();


                                final String clave = mDatabaseRef.push().getKey();
                                if (imagenSubida) {
                                    Uri selectedUri = mImageUri;

                                    StorageReference fotoRef = mStorageRefCas.child(selectedUri.getLastPathSegment());
                                    ut = fotoRef.putFile(selectedUri);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {

                                            ut.addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            centro = new Centro(clave, codCasa,  etNombre.getText().toString(), etEmail.getText().toString().toLowerCase(),etCiudad.getText().toString(), etDireccion.getText().toString(),validar);
                                                            mDatabaseRef.child(user.getUid()).setValue(centro);

                                                            /*Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            startActivity(i);*/


                                                            Query qq=mDatabaseRef.orderByChild("emailUsuario").equalTo(email);



                                                            qq.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                                mDatabaseRef.child("codCasa").setValue(codCasa);


                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }, 5000);

                                } else {

                                    centro = new Centro(clave, codCasa,  etNombre.getText().toString(), etEmail.getText().toString().toLowerCase(),etCiudad.getText().toString(), etDireccion.getText().toString(),validar);
                                    mDatabaseRef.child(user.getUid()).setValue(centro);

                                   /* Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(i);*/

                                }

                            } else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.msj_no_registrado), Toast.LENGTH_SHORT).show();

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

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeat.getText().toString().trim();
        nombre = etNombre.getText().toString().trim();
        ciudad = etCiudad.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            msj = "Debe introducirse el email y la password";
        }else if(nombre.isEmpty() || ciudad.isEmpty()) {
            msj = "Debe introducir el nombre y la ciudad";
        }else if (password.length() < 6) {
            msj = "La password debe contener al menos 6 caracteres";
        } else if (!password.equals(password2)) {
            msj = "Las contraseñas deben de coincidir";
        }

        return msj;
    }

    public String crearCodCasa(){

        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            conjunto[i] = (char)elementos[el];
        }
        return codigoCasa = new String(conjunto);
    }
}
