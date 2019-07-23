package com.osoc.oncera;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.osoc.oncera.javabean.Itinerary;
import com.osoc.oncera.javabean.Obstacles;
import com.osoc.oncera.javabean.Teacher;

import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class CreateItineraryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnLocationUpdatedListener {
    private Button btn_new, btn_confirm, btn_save;
    private Spinner spin;
    private ImageView imgCapture;
    private ImageView imgIcone;
    //private TextView tv_loc;
    private static final int Image_Capture_Code = 1;
    String[] access = { "Rampa", "Mostrador", "Ascensor", "Puerta", "Emergencias", "Luz", "Salvaescaleras", "Simulacion"};
    ArrayList<Obstacles> list_obst = new ArrayList<Obstacles>();

    private LocationGooglePlayServicesProvider provider;
    private Location currentLoc = new Location("");

    // TODO get teacher alias as extra from bundle and codCentro from Teacher alias

    private DatabaseReference mDatabaseRef;
    public static FirebaseAuth firebaseAuth;

    private FirebaseUser user;


    private String email;
    private String alias;
    private String codigo;
    private String titulo;
    private String descripcion;
    private String codItinerario;

    private final Teacher[] profesor = new Teacher[1];
    private final Itinerary[] itinerario = new Itinerary[1];

    private char[] conjunto = new char[6];
    char[] elementos={'0','1','2','3','4','5','6','7','8','9' ,'a',
            'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};

    private String codCentro = null;
    private String aliasProfesor = null;

    private String codigoItinerario;

    private int order = 1;

    private static final int LOCATION_PERMISSION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_itinerario);



        btn_new =(Button)findViewById(R.id.btn_new);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_save = (Button) findViewById(R.id.btn_save);
        imgCapture = (ImageView) findViewById(R.id.img_camera);
        imgIcone = (ImageView) findViewById( R.id.img_icon );
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnBack);
        //tv_loc = (TextView) findViewById(R.id.tv_loc);
        spin = (Spinner) findViewById(R.id.spin_access);
        spin.setOnItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        user = firebaseAuth.getCurrentUser();

        btn_confirm.setEnabled(false);
        btn_save.setEnabled(false);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,access);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        obtenerAlias();

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
                if (ContextCompat.checkSelfPermission( CreateItineraryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( CreateItineraryActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                    return;
                }
                startLocation();
                btn_confirm.setEnabled(true);
                btn_save.setEnabled(true);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO generate obstacle id
                String id = null;
                list_obst.add(new Obstacles(id,
                        currentLoc.getLongitude(),
                        currentLoc.getLatitude(),
                        spin.getSelectedItem().toString(),
                        imgCapture.getDrawable().toString(),
                        order,
                        codCentro));
                order++;
                btn_confirm.setEnabled(false);
                // TODO guardar Obstacles en firebase
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDialog();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);*/

        showLast();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(),access[position] , Toast.LENGTH_LONG).show();

        if (access[position] == "Rampa") {
            imgIcone.setImageResource(R.drawable.rampas);
        }
        else if (access[position] == "Mostrador") {
            imgIcone.setImageResource(R.drawable.mostradores);
        }
        else if (access[position] == "Ascensor") {
            imgIcone.setImageResource(R.drawable.ascensores);
        }
        else if (access[position] == "Puerta") {
            imgIcone.setImageResource(R.drawable.puertas);
        }
        else if (access[position] == "Emergencias") {
            imgIcone.setImageResource(R.drawable.emergencias);
        }
        else if (access[position] == "Luz") {
            imgIcone.setImageResource(R.drawable.iluminacion);
        }
        else if (access[position] == "Salvaescaleras") {
            imgIcone.setImageResource(R.drawable.salvaescaleras);
        }
        else if (access[position] == "Simulacion") {
            imgIcone.setImageResource(R.drawable.simulacion);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }
    }

    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
            currentLoc.setLongitude(lastLocation.getLongitude());
            currentLoc.setLatitude(lastLocation.getLatitude());
            /*tv_loc.setText(
                    String.format("[From Cache] Latitude %.6f, Longitude %.6f",
                            lastLocation.getLatitude(),
                            lastLocation.getLongitude())
            );*/
        }
    }


    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);

    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();

        SmartLocation.with(this).activity().stop();
    }

    private void showLocation(Location location) {
        if (location != null) {
            currentLoc.setLongitude(location.getLongitude());
            currentLoc.setLatitude(location.getLatitude());
            /*final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            tv_loc.setText(text);*/
            stopLocation();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    void guardarDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder( CreateItineraryActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_guardar_itinerario, null);

        EditText title = (EditText) mView.findViewById(R.id.et_title);
        EditText description = (EditText) mView.findViewById(R.id.et_desc);

        mBuilder.setTitle("Guardar Itinerario");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(title.getText()!=null && description.getText()!=null){
                    titulo = title.getText().toString().trim();
                    descripcion = description.getText().toString().trim();
                    guardarItinerario();
                    dialogInterface.dismiss();

                }else{
                    Toast.makeText( CreateItineraryActivity.this, "Debes introducir Titulo y Desripcion", Toast.LENGTH_SHORT ).show();
                }
                    //saveItinerario(title.getText().toString(), description.getText().toString());

            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }

   /* public void saveItinerario(String title, String desc){
        String id = null;
        Itinerario iter = new Itinerario(id, list_obst, aliasProfesor, title, desc, codCentro,codItinerario);

        // TODO save itinerary in firebase
        finish();
    }*/

    public void obtenerAlias(){
        email = user.getEmail();
        Query qq4 = mDatabaseRef.orderByChild( "mail" ).equalTo( email );

        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    profesor[0] = dataSnapshot1.getValue( Teacher.class );
                }

                if (email.equals( profesor[0].getMail() )) {

                    alias = profesor[0].getAlias();
                    codigo = profesor[0].getCenterCode();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( CreateItineraryActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }
    public void guardarItinerario(){

        boolean[] repetido={false};

        do {
            repetido[0]=false;
            codItinerario = crearcodItinerario();
            Query qq4 = mDatabaseRef.orderByChild( "ItineraryCode" ).equalTo( codItinerario ).limitToFirst( 1 );
            qq4.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        itinerario[0] = dataSnapshot1.getValue( Itinerary.class );
                    }

                    if (itinerario[0] != null) {

                        if (itinerario[0].getItineraryCode().equals( codItinerario ) && codItinerario != null) {
                            Toast.makeText( CreateItineraryActivity.this, "Generando codigo Itinerario", Toast.LENGTH_LONG ).show();
                            repetido[0] = true;
                        }

                    }

                    //qq4.removeEventListener( this );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText( CreateItineraryActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

                }
            } );
        } while(repetido[0] == true);

        final DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Itineraries");


        final String clave = mDatabaseRef2.push().getKey();

        Itinerary iti = new Itinerary( clave, list_obst, alias, titulo, descripcion,codigo, codItinerario );
        mDatabaseRef2.child( clave ).setValue( iti );
        //mDatabaseRef2.push();



    }
    public String crearcodItinerario(){

        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            conjunto[i] = (char)elementos[el];
        }
        return codigoItinerario = new String(conjunto);
    }

    public String crearcodItinerario2(){

        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            conjunto[i] = (char)elementos[el];
        }
        return codigoItinerario = new String(conjunto);
    }
}