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
    private ImageView imgIcon;

    private static final int Image_Capture_Code = 1;
    String[] access = { "Rampa", "Mostrador", "Ascensor", "Puerta", "Emergencias", "Luz", "Salvaescaleras", "Simulacion"};
    ArrayList<Obstacles> list_obst = new ArrayList<Obstacles>();

    private LocationGooglePlayServicesProvider provider;
    private Location currentLoc = new Location("");

    // TODO get teacher teacher_alias as extra from bundle and centerCode from Teacher teacher_alias

    private DatabaseReference mDatabaseRef;
    public static FirebaseAuth firebaseAuth;

    private FirebaseUser user;


    private String teacher_email;
    private String teacher_alias;
    private String myCenterCode;
    private String title;
    private String description;
    private String itineraryCode;

    private final Teacher[] teacher = new Teacher[1];
    private final Itinerary[] itinerary = new Itinerary[1];

    private char[] itineraryCodePlaceholder = new char[6];
    char[] elements ={'0','1','2','3','4','5','6','7','8','9' ,'a',
            'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};

    private String centerCode = null;

    private int order = 1;

    private static final int LOCATION_PERMISSION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);



        btn_new =(Button)findViewById(R.id.btn_new);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_save = (Button) findViewById(R.id.btn_save);
        imgCapture = (ImageView) findViewById(R.id.img_camera);
        imgIcon = (ImageView) findViewById( R.id.img_icon );
        ImageButton btnAtras = (ImageButton) findViewById(R.id.btnBack);

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


        getAlias();

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission( CreateItineraryActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( CreateItineraryActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                    return;
                }

                if (ContextCompat.checkSelfPermission( CreateItineraryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( CreateItineraryActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }

                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
                startLocation();
                btn_confirm.setEnabled(true);
                btn_save.setEnabled(true);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // TODO generate obstacle id
                String id = null;
                list_obst.add(new Obstacles(id,
                        currentLoc.getLongitude(),
                        currentLoc.getLatitude(),
                        spin.getSelectedItem().toString(),
                        imgCapture.getDrawable().toString(),
                        order,
                        centerCode));
                order++;
                btn_confirm.setEnabled(false);
                // TODO guardar Obstacles en firebase

            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItineraryDialog();


            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
            imgIcon.setImageResource(R.drawable.rampas);
        }
        else if (access[position] == "Mostrador") {
            imgIcon.setImageResource(R.drawable.mostradores);
        }
        else if (access[position] == "Ascensor") {
            imgIcon.setImageResource(R.drawable.ascensores);
        }
        else if (access[position] == "Puerta") {
            imgIcon.setImageResource(R.drawable.puertas);
        }
        else if (access[position] == "Emergencias") {
            imgIcon.setImageResource(R.drawable.emergencias);
        }
        else if (access[position] == "Luz") {
            imgIcon.setImageResource(R.drawable.iluminacion);
        }
        else if (access[position] == "Salvaescaleras") {
            imgIcon.setImageResource(R.drawable.salvaescaleras);
        }
        else if (access[position] == "Simulacion") {
            imgIcon.setImageResource(R.drawable.simulacion);
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

    /**
     * Show last location of the user
     */
    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
            currentLoc.setLongitude(lastLocation.getLongitude());
            currentLoc.setLatitude(lastLocation.getLatitude());
        }
    }

    /**
     * Start listening for the location of the user
     */
    private void startLocation() {
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);

    }

    /**
     * Stop listening for the location of the user
     */
    private void stopLocation() {
        SmartLocation.with(this).location().stop();

        SmartLocation.with(this).activity().stop();
    }

    private void showLocation(Location location) {
        if (location != null) {
            currentLoc.setLongitude(location.getLongitude());
            currentLoc.setLatitude(location.getLatitude());
            stopLocation();
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    /**
     * Dialog to ask for a title and description and save the itinerary
     */
    void saveItineraryDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder( CreateItineraryActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_save_itinerary, null);

        EditText title = (EditText) mView.findViewById(R.id.et_title);
        EditText description = (EditText) mView.findViewById(R.id.et_desc);

        mBuilder.setTitle("Guardar Itinerario");
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);


        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(title.getText()!=null && description.getText()!=null){
                    CreateItineraryActivity.this.title = title.getText().toString().trim();
                    CreateItineraryActivity.this.description = description.getText().toString().trim();
                    saveItinerary();
                    dialogInterface.dismiss();
                    finish();


                }else{
                    Toast.makeText( CreateItineraryActivity.this, "Debes introducir Titulo y Desripcion", Toast.LENGTH_SHORT ).show();
                }
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

    /**
     * Get the teacher's alias
     */
    public void getAlias(){
        teacher_email = user.getEmail();
        Query qq4 = mDatabaseRef.orderByChild( "mail" ).equalTo(teacher_email);

        qq4.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    teacher[0] = dataSnapshot1.getValue( Teacher.class );
                }

                if (teacher_email.equals( teacher[0].getMail() )) {

                    teacher_alias = teacher[0].getAlias();
                    myCenterCode = teacher[0].getCenterCode();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( CreateItineraryActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    /**
     * save the itinerary with a unique itineraryCode
     */
    public void saveItinerary(){
        boolean[] repeated={false};

        do {
            repeated[0]=false;
            itineraryCode = createItineraryCode();
            Query qq4 = mDatabaseRef.orderByChild( "ItineraryCode" ).equalTo(itineraryCode).limitToFirst( 1 );
            qq4.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        itinerary[0] = dataSnapshot1.getValue( Itinerary.class );
                    }

                    if (itinerary[0] != null) {

                        if (itinerary[0].getItineraryCode().equals(itineraryCode) && itineraryCode != null) {
                            Toast.makeText( CreateItineraryActivity.this, "Generando tvCode Itinerario", Toast.LENGTH_LONG ).show();
                            repeated[0] = true;
                        }

                    }

                    //qq4.removeEventListener( this );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText( CreateItineraryActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

                }
            } );
        } while(repeated[0] == true);

        final DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Itineraries");


        final String key = mDatabaseRef2.push().getKey();

        Itinerary iti = new Itinerary( key, list_obst, teacher_alias, title, description, myCenterCode, itineraryCode);
        mDatabaseRef2.child( key ).setValue( iti );
    }

    /**
     * Create a random itinerary code
     * @return string containing the new itinerary code created
     */
    public String createItineraryCode(){

        for(int i=0;i<6;i++){
            int el = (int)(Math.random()*36);
            itineraryCodePlaceholder[i] = (char) elements[el];
        }
        return itineraryCode = new String(itineraryCodePlaceholder);
    }

}