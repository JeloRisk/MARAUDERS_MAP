//package com.example.marauders_map;
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//public class newMap extends AppCompatActivity implements OnMapReadyCallback {
//
//    private MapView mapView;
//    private MapboxMap mapboxMap;
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_new_map);
////        Mapbox.getInstance(newMap.this, getString(R.string.mapbox_access_token));
////        mapView = findViewById(R.id.mapView);
////        mapView.onCreate(savedInstanceState);
////
////        mapView.getMapAsync(this);
////    }
////
////    @Override
////    public void onMapReady(MapboxMap mapboxMap) {
////        mapboxMap.setStyle(Style.MAPBOX_STREETS);
////    }
////
////    @Override
////    protected void onStart() {
////        super.onStart();
////        mapView.onStart();
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        mapView.onResume();
////    }
////
////    @Override
////    protected void onPause() {
////        super.onPause();
////        mapView.onPause();
////    }
////
////    @Override
////    protected void onStop() {
////        super.onStop();
////        mapView.onStop();
////    }
////
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        mapView.onDestroy();
////    }
////
////    @Override
////    public void onLowMemory() {
////        super.onLowMemory();
////        mapView.onLowMemory();
////    }
//}