package com.osoc.oncera;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.osoc.oncera.javabean.Itinerary;
import com.osoc.oncera.javabean.Obstacles;
import com.osoc.oncera.javabean.StairLifter;

import java.util.ArrayList;

public class MapaItinerarioActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;

    private DatabaseReference mDatabaseRef;

    private Button btn_evaluar;
    private Spinner sp_obstaculo;


    private String nombre;
    private double longitud;
    private double latitud;
    private String tipo;
    private int orden;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    ArrayList<Feature> col = new ArrayList<>();
    private GeoJsonSource geoJsonSource;

    private final Itinerario[] iti = new Itinerario[1];
    private ArrayList<Obstaculo> obs;


    private LocationListener locationListener;
    private LocationManager locationManager;

    private String codigoItinerario;

    private Itinerario itinerario;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiZm9uY2UiLCJhIjoiY2p4b3B1NG53MDhsbTNjbnYzMXNpbjRjYiJ9.MkBM2G0smC9aOJ_IS804xg");
        setContentView(R.layout.activity_mapa_itinerario);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Itinerarios" );

        Bundle bundle = getIntent().getExtras();
        codigoItinerario = bundle.getString( "codigoItinerario" );

        btn_evaluar = (Button) findViewById(R.id.btn_evaluar);
        sp_obstaculo = (Spinner) findViewById(R.id.sp_obstaculo);

        String[] list = new String[itinerario.getObstaculos().size()];

        for(int i = 0; i < itinerary.getObstacles().size() ; i++){
            list[i]= "Obstacles: "+Integer.toString(i+1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);

        sp_obstaculo.setAdapter(adapter);


        btn_evaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEv();
            }
        });
    }

    private void goToEv()
    {
        Intent i;
        String t = itinerary.getObstacles().get((int)sp_obstaculo.getSelectedItemId()).getType();
        if (t == TypesManager.DOOR_OBS)  i = new Intent(this, MeasureDoor.class);
        else if (t == TypesManager.ILLUM_OBS)i = new Intent(this, LuxMeter.class);
        else if (t == TypesManager.ASCENSOR_OBS) i = new Intent(this, MeasureElevator.class);
        else if (t == TypesManager.MOSTRADOR_OBS) i = new Intent(this, MeasureCounter.class);
        else if (t == TypesManager.RAMPA_OBS) i = new Intent(this, MeasureRamp.class);
        else if (t == TypesManager.SALVAESC_OBS) i = new Intent(this, StairLifter.class);
        else if (t == TypesManager.EMERGENC_OBS) i = new Intent(this, MeasureEmergencies.class);

        else i = new Intent(this,null);

        startActivity(i);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        MapaItinerarioActivity.this.mapboxMap = mapboxMap;

        LatLng BOUND_CORNER_NW = new LatLng(itinerary.getObstacles().get(0).getLatitude()+0.002, itinerary.getObstacles().get(0).getLength()+0.002);
        LatLng BOUND_CORNER_SE = new LatLng(itinerary.getObstacles().get(0).getLatitude()-0.002, itinerary.getObstacles().get(0).getLength()-0.002);
        LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
                .include(BOUND_CORNER_NW)
                .include(BOUND_CORNER_SE)
                .build();
        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
        mapboxMap.setMinZoomPreference(16);


        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(itinerary.getObstacles().get(0).getLatitude(), itinerary.getObstacles().get(0).getLength()))
                .zoom(16)
                .build());



        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                style.addImage(ICON_ID, BitmapFactory.decodeResource(
                        MapaItinerarioActivity.this.getResources(), R.drawable.map_marker));

                for(int i = 0; i< itinerary.getObstacles().size(); i++) {
                    col.add(Feature.fromGeometry(Point.fromLngLat(itinerary.getObstacles().get(i).getLength(), itinerary.getObstacles().get(i).getLatitude())));
                    col.get(col.size()-1).addStringProperty("poi", ICON_ID);
                    col.get(col.size()-1).addStringProperty("title", Integer.toString(i+1));
                }


                geoJsonSource = new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(col));
                style.addSource(geoJsonSource);


                SymbolLayer symbolLayer = new SymbolLayer(LAYER_ID, SOURCE_ID);

                symbolLayer.withProperties(
                        PropertyFactory.iconImage("{poi}"),
                        PropertyFactory.iconSize(0.40f),
                        PropertyFactory.textField("{title}"),
                        PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
                        PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                        PropertyFactory.textAllowOverlap(true),
                        PropertyFactory.iconAllowOverlap(true)
                );

                style.addLayer(symbolLayer);
            }
        });
    }

    // Add the mapView's own lifecycle methods to the activity's lifecycle methods
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void cargarItinerario(){

        Query qq2 = mDatabaseRef.orderByChild( "codItinerario" ).equalTo( codigoItinerario ).limitToFirst( 1 );
        qq2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    iti[0] = dataSnapshot1.getValue( Itinerario.class );
                }

                if (iti[0] != null) {

                    if (iti[0].getCodItinerario().equals( codigoItinerario ) && codigoItinerario != null) {
                        Toast.makeText( MapaItinerarioActivity.this, "Codigo correcto", Toast.LENGTH_LONG ).show();

                       obs = iti[0].getObstaculos();
                        //TODO HACER FOR EACH PARA SACAR LOS OBSTACULOS DE FIREBASE Y QUE EL ALUMNO PUEDO DESCARGAR EL ITINERARIO.
                       /*for (Obstaculo cosas: obs){
                           obs.get( cosas )

                       }*/

                    } else {
                        Toast.makeText( MapaItinerarioActivity.this, "El Código no Existe", Toast.LENGTH_LONG ).show();
                    }

                } else {
                    Toast.makeText( MapaItinerarioActivity.this, "Codigo Incorrecto", Toast.LENGTH_LONG ).show();
                }

                qq2.removeEventListener( this );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( MapaItinerarioActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );

    }


}

