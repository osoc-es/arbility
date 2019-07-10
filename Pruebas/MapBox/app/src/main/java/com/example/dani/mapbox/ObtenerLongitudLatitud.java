package com.example.dani.mapbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

public class ObtenerLongitudLatitud extends AppCompatActivity {

    Button gps;
    TextView tvLongitud;
    TextView tvLatitud;

    double longitud;
    double latitud;

    private LocationListener locationListener;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_obtener_longitud_latitud );

        gps = (Button) findViewById( R.id.btnUbi );
        tvLongitud = (TextView) findViewById( R.id.tvLongitud );
        tvLatitud = (TextView) findViewById( R.id.tvLatitud );

        //validacion();
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                    @Override
                    public void gotLocation(Location location){
                        longitud = location.getLongitude();
                        latitud = location.getLatitude();

                        Intent i = new Intent( ObtenerLongitudLatitud.this, MainActivity.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        Bundle b = new Bundle();
                        b.putDouble("longitud", longitud);
                        b.putDouble("latitud", latitud);

                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(b);
                        startActivity(i);
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(ObtenerLongitudLatitud.this, locationResult);
            }
        });

    }

    public void btnMapa(View v){
        obtenerUbi();
        gps.setEnabled( false );
    }

    private void validacion() {
        int permissionCheck = ContextCompat.checkSelfPermission( ObtenerLongitudLatitud.this,
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

        locationManager = (LocationManager) ObtenerLongitudLatitud.this.getSystemService( Context.LOCATION_SERVICE );

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvLongitud.setText( Double.toString( location.getLongitude() ));
                tvLatitud.setText( Double.toString (location.getLatitude() ));

                longitud = location.getLongitude();
                latitud = location.getLatitude();

                Intent i = new Intent( ObtenerLongitudLatitud.this, MainActivity.class ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                Bundle b = new Bundle();
                b.putDouble("longitud", longitud);
                b.putDouble("latitud", latitud);

                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtras(b);
                startActivity(i);
                end();
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


        int permissionCheck = ContextCompat.checkSelfPermission( ObtenerLongitudLatitud.this,
                Manifest.permission.ACCESS_FINE_LOCATION );

        locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener );
    }

    public void end(){
        //locationManager.removeUpdates(locationListener);
        this.finish();
    }
}
