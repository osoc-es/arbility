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

    ArrayList<Double> al_longitud = new ArrayList<Double>();
    ArrayList<Double> al_latitud = new ArrayList<Double>();

    private final Itinerary[] iti = new Itinerary[1];
    private ArrayList<Obstacles> obs;


    private LocationListener locationListener;
    private LocationManager locationManager;

    private String codigoItinerario;

    private Itinerary itinerario;



    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        codigoItinerario = bundle.getString( "itineraryCode" );
        cargarItinerario();

        Mapbox.getInstance(this, "pk.eyJ1IjoiZm9uY2UiLCJhIjoiY2p4b3B1NG53MDhsbTNjbnYzMXNpbjRjYiJ9.MkBM2G0smC9aOJ_IS804xg");
        setContentView(R.layout.activity_mapa_itinerario);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child( "Itineraries" );



        btn_evaluar = (Button) findViewById(R.id.btn_evaluar);
        sp_obstaculo = (Spinner) findViewById(R.id.sp_obstaculo);

        //String[] list = new String[itinerario.getObstaculos().size()];

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
        String t = obs.get(sp_obstaculo.getSelectedItemPosition()).getType();
        if (t.equals(TypesManager.DOOR_OBS)) i = new Intent(this, MeasureDoor.class);
        else if (t.equals(TypesManager.ILLUM_OBS))i = new Intent(this, LuxMeter.class);
        else if (t.equals(TypesManager.ELEVATOR_OBS)) i = new Intent(this, MeasureElevator.class);
        else if (t.equals(TypesManager.ATTPOINT_OBS)) i = new Intent(this, MeasureCounter.class);
        else if (t.equals(TypesManager.RAMP_OBS)) i = new Intent(this, MeasureRamp.class);
        else if (t.equals(TypesManager.STAIRLIFTER_OBS)) i = new Intent(this,StairLifter.class);
        else if (t.equals(TypesManager.EMERGENCY_OBS)) i = new Intent(this, MeasureEmergencies.class);
        else if (t.equals(TypesManager.SIMULATION_OBS)) i = new Intent(this, WheelchairSimulation.class);

        else i = new Intent(this,null);

        startActivity(i);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        MapaItinerarioActivity.this.mapboxMap = mapboxMap;

        /*LatLng BOUND_CORNER_NW = new LatLng(itinerario.getObstaculos().get(0).getLatitud()+0.002, itinerario.getObstaculos().get(0).getLongtiud()+0.002);
        LatLng BOUND_CORNER_SE = new LatLng(itinerario.getObstaculos().get(0).getLatitud()-0.002, itinerario.getObstaculos().get(0).getLongtiud()-0.002);
        LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
                .include(BOUND_CORNER_NW)
                .include(BOUND_CORNER_SE)
                .build();
        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
        mapboxMap.setMinZoomPreference(16);


        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(itinerario.getObstaculos().get(0).getLatitud(), itinerario.getObstaculos().get(0).getLongtiud()))
                .zoom(16)
                .build());
*/


        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                style.addImage(ICON_ID, BitmapFactory.decodeResource(
                        MapaItinerarioActivity.this.getResources(), R.drawable.map_marker));



                LatLng BOUND_CORNER_NW = new LatLng(obs.get( 0 ).getLatitude()+0.002, obs.get( 0 ).getLength()+0.002);
                LatLng BOUND_CORNER_SE = new LatLng(obs.get( 0 ).getLatitude()-0.002, obs.get( 0 ).getLength()-0.002);
                LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
                        .include(BOUND_CORNER_NW)
                        .include(BOUND_CORNER_SE)
                        .build();
                mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
                mapboxMap.setMinZoomPreference(16);


                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(obs.get( 0 ).getLatitude(), obs.get( 0 ).getLength()))
                        .zoom(16)
                        .build());


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

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Itineraries");

        Query qq = mDatabaseRef.orderByChild( "itineraryCode" ).equalTo( codigoItinerario ).limitToFirst( 1 );

        qq.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    iti[0] = dataSnapshot1.getValue( Itinerary.class );
                }

              // if (iti[0] != null) {

                    if (iti[0].getItineraryCode().equals( codigoItinerario )) {
                       String prueba = iti[0].getItineraryCode();
                        Toast.makeText( MapaItinerarioActivity.this, prueba, Toast.LENGTH_LONG ).show();

                        obs = iti[0].getObstacles();
                        //TODO HACER FOR EACH PARA SACAR LOS OBSTACULOS DE FIREBASE Y QUE EL ALUMNO PUEDO DESCARGAR EL ITINERARIO.
                       String[] list = new String[obs.size()];

                       for (int i = 0; i<obs.size();i++){
                           col.add(Feature.fromGeometry(Point.fromLngLat(obs.get(i).getLength(), obs.get(i).getLatitude())));
                           col.get(col.size()-1).addStringProperty("poi", ICON_ID);
                           col.get(col.size()-1).addStringProperty("title", Integer.toString( obs.get(i).getOrder()));
                           list[i]= "Obstaculo: "+obs.get(i).getOrder();

                       }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapaItinerarioActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,list);

                        sp_obstaculo.setAdapter(adapter);

                    } else {
                        Toast.makeText( MapaItinerarioActivity.this, "AAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_LONG ).show();
                    }

                //} else {
                //    Toast.makeText( MapaItinerarioActivity.this, "AAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_LONG ).show();
               // }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( MapaItinerarioActivity.this, "Algo salio Mal ahí", Toast.LENGTH_SHORT ).show();

            }
        } );

    }


}

