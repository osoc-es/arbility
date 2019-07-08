package com.example.dani.mapbox;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private MapboxMap mapboxMap;

    double longitud;
    double latitud;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiZm9uY2UiLCJhIjoiY2p4b3B1NG53MDhsbTNjbnYzMXNpbjRjYiJ9.MkBM2G0smC9aOJ_IS804xg");
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        MainActivity.this.mapboxMap = mapboxMap;
        Bundle bu = getIntent().getExtras();
        longitud = bu.getDouble( "longitud" );
        latitud = bu.getDouble( "latitud" );
        bu.clear();


        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(latitud, longitud))
                .zoom(16)
                .build());

        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                style.addImage("marker-icon-id", BitmapFactory.decodeResource(
                        MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

                GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                        Point.fromLngLat(longitud, latitud)));
                style.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                symbolLayer.withProperties(
                        PropertyFactory.iconImage("marker-icon-id")
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
}

