package com.example.dani.prueba_osm_dani;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    private MapView osm;
    private IMapController mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        osm = (MapView) findViewById( R.id.mapView );
        osm.setTileSource( TileSourceFactory.MAPNIK );
        osm.setBuiltInZoomControls( true );
        osm.setMultiTouchControls( true );

        mc = (MapController) osm.getController();
        mc.setZoom( 18 );

        GeoPoint center = new GeoPoint( 40.3472596, -3.6961396 );
        mc.setCenter( center );



    }


    public void addMarker(GeoPoint center){
        Marker marker = new Marker(osm);
        marker.setPosition( center );
        marker.setAnchor( Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM );
        marker.setIcon( getResources().getDrawable( R.drawable.yo) );

        osm.getOverlays().clear();
        osm.getOverlays().add( marker );
        osm.invalidate();

    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        osm.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        osm.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
