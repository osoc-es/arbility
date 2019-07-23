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

public class RegisterInstitutionActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etCity;
    private EditText etEmail;
    private EditText etAddress;
    private EditText etPassword, etPasswordRepeat;
    private ProgressDialog progressDialog;
    private Button btnRegister;
    private Button btnValidate;


    private DatabaseReference mDatabaseRef;
    public static FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String email;

    private String password;
    private String password2;
    private String name;
    private String city;
    private String address;
    private Boolean validateCenter;

    private String emailDani;


    private Institution institution;

    private String centerCode;

    private char[] centerCodePlaceholder = new char[6];
    //Possible characters to be used in the institution's code
    char[] elements ={'0','1','2','3','4','5','6','7','8','9' ,'a',
            'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    String codigoCentro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        validateCenter = false;
        etName = (EditText) findViewById(R.id.etNameReg);
        etCity = (EditText) findViewById(R.id.etCityReg);
        etAddress = (EditText) findViewById( R.id.etAddress);
        etEmail = (EditText) findViewById(R.id.etEmailReg);
        etPassword = (EditText) findViewById(R.id.etPasswordReg);
        etPasswordRepeat = (EditText) findViewById(R.id.etPasswordRepeat);
        btnRegister = (Button) findViewById( R.id.btnRegisterInstitution);
        btnValidate = (Button) findViewById( R.id.btnValidate);




        //progressDialog = new ProgressDialog(this);
        emailDani = "danisom1b@gmail.com";
    }

    /**
     * Save new Institution in the database or generate an error message
     * @param v view taken as a parameter
     */
    private void register(View v) {
        String warning = validarDatos();

        if (warning == null) {
            //progressDialog.setMessage( "Realizando registro en linea. . ." );
            //  progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                centerCode = createCodeCenter();
                                mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("Users");

                                user = firebaseAuth.getCurrentUser();


                                String keyword = user.getUid();

                                    institution = new Institution(keyword, centerCode,  etName.getText().toString(), etEmail.getText().toString().toLowerCase(), etCity.getText().toString(), etAddress.getText().toString(), validateCenter);
                                    mDatabaseRef.child(user.getUid()).setValue(institution);
                                    Toast.makeText( RegisterInstitutionActivity.this, "Registrado Correcto, Valídalo para porder Loguearte", Toast.LENGTH_LONG ).show();

                                    btnRegister.setVisibility(View.INVISIBLE);


                            } else {
                                Toast.makeText( RegisterInstitutionActivity.this, getString(R.string.msj_no_registrado), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        } else {
            Toast.makeText(this, warning,
                    Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

    }


    private void validar(View v){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse( "mailto:" + emailDani) );
        i.putExtra(Intent.EXTRA_SUBJECT, etName.getText().toString());
        i.putExtra(Intent.EXTRA_TEXT, etAddress.getText().toString());
        startActivity( i );
    }

    /**
     * Check whether all the data entered is correct
     * @return the error message if some value is missing
     */
    private String validarDatos() {
        String msj = null;

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password2 = etPasswordRepeat.getText().toString().trim();
        name = etName.getText().toString().trim();
        city = etCity.getText().toString().trim();
        address = etAddress.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            msj = "Debe introducirse el email y la password";
        }else if (name.isEmpty() || city.isEmpty() || address.isEmpty()) {
            msj = "Debe introducir el name, la city y la address";
        }else if (password.length() < 6) {
            msj = "La password debe contener al menos 6 caracteres";
        } else if (!password.equals(password2)) {
            msj = "Las contraseñas deben de coincidir";
        }

        return msj;
    }

    /**
     * Create an institution code randomly
     * @return String with the value of the institution's code
     */
    private String createCodeCenter(){
        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            centerCodePlaceholder[i] = (char) elements[el];
        }
        return codigoCentro = new String(centerCodePlaceholder);
    }
}
