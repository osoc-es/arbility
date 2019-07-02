package com.example.dani.pruebaubicacionlatitudlongitud;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button gps;
    TextView ubi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        gps = (Button) findViewById( R.id.btnUbi );
        ubi = (TextView) findViewById( R.id.tvUbicacion );

        validacion();

        gps.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerUbi();

            }
        } );






    }

    private void validacion() {
        int permissionCheck = ContextCompat.checkSelfPermission( MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION );

        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
        }
    }

    private void obtenerUbi() {
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService( Context.LOCATION_SERVICE );

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ubi.setText( "" + location.getLatitude() + " " + location.getLongitude() );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission( MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION );


        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener );
    }

}
