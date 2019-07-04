package com.example.osm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Marker;


public class MainActivity extends AppCompatActivity  {
    private MapView map = null;
    private MapController mc;
    private LocationManager locationManager;


    @SuppressLint("ServiceCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Context ctx = getApplicationContext();
        Configuration.getInstance().load( ctx, PreferenceManager.getDefaultSharedPreferences( ctx ) );

        setContentView( R.layout.activity_main );

        map = (MapView) findViewById( R.id.map );
        map.setTileSource( TileSourceFactory.MAPNIK);

       // map.setMultiTouchControls( true );
        IMapController mapController = map.getController();
        mapController.setZoom( 21.0 );
        GeoPoint startPoint = new GeoPoint( 40.3472596, -3.6961396 );
        mapController.setCenter( startPoint );
        addMarker1( startPoint );

       /* GeoPoint puerta = new GeoPoint( 40.3481172, -3.6960068 );
        //addmarker2( puerta );

        GeoPoint rampa = new GeoPoint( 40.3474814, -3.6956763 );
        //addMarker3( rampa );*/
/*
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );
     */
    }
    public void addMarker1(GeoPoint startPoint) {
        Marker marker = new Marker( map );
        marker.setPosition( startPoint );
        // marker.setAnchor( Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM );
        marker.setIcon( getResources().getDrawable( R.drawable.senial ) );

        //map.getOverlays().clear();
        map.getOverlays().add( marker );
        //map.invalidate();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(startPoint);

    }
   /* public void addmarker2(GeoPoint puerta) {
        Marker marker2 = new Marker( map );
        marker2.setPosition( puerta );
        marker2.setAnchor( Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM );
        marker2.setIcon( getResources().getDrawable( R.drawable.senial ) );

       // map.getOverlays().clear();
        map.getOverlays().add( marker2 );
        map.invalidate();

    }
    public void addMarker3(GeoPoint rampa) {
        Marker marker3 = new Marker( map );
        marker3.setPosition( rampa );
        marker3.setAnchor( Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM );
        marker3.setIcon( getResources().getDrawable( R.drawable.senial ) );

        // map.getOverlays().clear();
        map.getOverlays().add( marker3 );
        map.invalidate();

    }*/
       /* public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
/*
    @Override
    public void onLocationChanged(Location location) {

        GeoPoint center = new GeoPoint( location.getLatitude(), location.getLongitude() );
        mc.setCenter( center );
        addMarker1( center );
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates( this );
        }
    }*/
}
