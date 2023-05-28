package com.example.proyectomovil.activities;

import android.content.Context;


import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.FragmentHomeBinding;
import com.example.proyectomovil.services.GeoInfoFromJsonService;
import com.example.proyectomovil.services.GeocoderService;
import com.example.proyectomovil.services.LocationService;
import com.example.proyectomovil.utils.BitmapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;


public class HomeFragment extends Fragment  implements OnMapReadyCallback {

    @Inject
    GeoInfoFromJsonService geoInfoFromJsonService;

    @Inject
    GeocoderService geocoderService;

    @Inject
    LocationService locationService;

    public FragmentHomeBinding binding;

    GoogleMap mMap;

    private Marker marker;
    static final int INITIAL_ZOOM_LEVEL = 18;

    private final LatLng UNIVERSIDAD = new LatLng(4.628150, -74.064227);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapview, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar opciones del mapa
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().isMyLocationButtonEnabled();
        // Mover y marcar una ubicación en el mapa
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UNIVERSIDAD, INITIAL_ZOOM_LEVEL));
        mMap.addMarker(new MarkerOptions().position(UNIVERSIDAD)
                .icon(BitmapUtils.getBitmapDescriptor(getContext(), R.drawable.baseline_person_pin_circle_24))
                .title("Universidad")
                .anchor(0.5f, 1.0f)
                .zIndex(1.0f));
        googleMap.setOnMapLongClickListener(latLng -> {
            if (marker != null) {
                marker.remove();
            }
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Ubicación seleccionada")
                    .snippet(latLng.toString());
            marker = googleMap.addMarker(markerOption);
            //dibujarRutas(userPosition.getPosition(),marker.getPosition());
        });
    }

//    private void showCurrentLocation() {
//         Verificar si el permiso de ubicación está concedido
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
//             Se tiene permiso, obtener la ubicación actual
//            LocationListener locationListener = location -> {
//                 Aquí se recibe la ubicación actual
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                 Utiliza la latitud y longitud como desees
//                Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
//            };
//             Actualizar la ubicación cada cierto tiempo y distancia
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, (android.location.LocationListener) locationListener);
//        } else {
//             El permiso de ubicación no está concedido, solicitarlo al usuario
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION}, new );
//        }
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        userRoute = googleMap.addPolyline(new PolylineOptions()
//                .color(R.color.green_500)
//                .width(30.0f)
//                .geodesic(true)
//                .zIndex(0.5f));
     //   Setup the rest of the markers based in a json file
//        googleMap.setOnMapLongClickListener(latLng -> {
//            if (marker != null) {
//                marker.remove();
//            }
//            MarkerOptions markerOption = new MarkerOptions();
//            markerOption.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Ubicación seleccionada")
//                    .snippet(latLng.toString());
//            marker = googleMap.addMarker(markerOption);
//            dibujarRutas(userPosition.getPosition(),marker.getPosition());
//        });
//    }
//
//    @Override
//    public void onMapLongClick(@NonNull LatLng latLng) {
//        if (marker != null) {
//            marker.remove();
//        }
//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Ubicación seleccionada")
//                .snippet(latLng.toString());
//        marker = googleMap.addMarker(markerOption);
//        dibujarRutas(userPosition.getPosition(),marker.getPosition());
//    }
   /* @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        binding = FragmentHomeBinding.inflate(inflater);
//        return binding.getRoot();
//    }
//
        binding.materialButton.setOnClickListener(view1 -> findPlaces());
//        /*binding.textInputLayout.getEditText().setOnEditorActionListener((textView, i, keyEvent) -> {
//            if(i == EditorInfo.IME_ACTION_SEARCH){
//                findPlaces();
//                return true;
//            }
//            return false;
//        });*/
//   /* private void findPlaces(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            places.forEach(marker -> marker.remove());
//            places.clear();
//            try {
//                geocoderService.finPlacesByNameInRadius(binding.textInputLayout.getEditText().getText().toString(), userPosition.getPosition()).forEach(address -> {
//                    Marker tmp = googleMap.addMarker(new MarkerOptions()
//                            .title(address.getFeatureName())
//                            .snippet(address.getAddressLine(0))
//                            .position(new LatLng(address.getLatitude(), address.getLongitude())));
//                    places.add(tmp);
//
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(places.get(0).getPosition()));
//                });
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }*/
//
//    /*@Override
//    public void onStart() {
//        super.onStart();
//        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        lightSensorEventListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(@NotNull SensorEvent sensorEvent) {
//                if(googleMap != null){
//                    if (sensorEvent.values[0] > 1000) {
//                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_day_style));
//                    } else {
//                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night_style));
//                    }
//                }
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };
//        sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
//
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        List<LatLng> points = userRoute.getPoints();
//        points.clear();
//        userRoute.setPoints(points);
//    }
//
//    private void loadGeoInfo() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            geoInfoFromJsonService.getGeoInfoList().forEach(geoInfo -> {
//                MarkerOptions newMarker = new MarkerOptions();
//                newMarker.position(new LatLng(geoInfo.getLat(), geoInfo.getLng()));
//                newMarker.title(geoInfo.getTitle());
//                newMarker.snippet(geoInfo.getContent());
//                if (geoInfo.getImageBase64() != null) {
//                    byte[] pinImage = Base64.decode(geoInfo.getImageBase64(), Base64.DEFAULT);
//                    Bitmap decodedPin = BitmapFactory.decodeByteArray(pinImage, 0, pinImage.length);
//                    Bitmap smallPin = Bitmap.createScaledBitmap(decodedPin, 200, 200, false);
//                    newMarker.icon(BitmapDescriptorFactory.fromBitmap(smallPin));
//                }
//                googleMap.addMarker(newMarker);
//            });
//        }
//    }
//
//    public void updateUserPositionOnMap(@NotNull LocationResult locationResult) {
//        userPosition.setPosition(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()));
//        List<LatLng> points = userRoute.getPoints();
//        points.add(userPosition.getPosition());
//
//        userRoute.setPoints(points);
//        if (binding.isCameraFixedToUser.isChecked()) {
//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(userPosition.getPosition()));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition.getPosition(), INITIAL_ZOOM_LEVEL));
//        }
//    }
//
//
//
//
//
//
//    /*public void dibujarRutas(LatLng origen, LatLng destino){
//        RequestQueue mRequestQueue = Volley.newRequestQueue(MapsFragment.this.getContext());
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
//                origen.latitude + "," + origen.longitude + "&destination=" +
//                destino.latitude + "," + destino.longitude + "&key=" + "AIzaSyBqx3TGwxFg_QrQDeN6Gpmx5p-dOzTWIwg";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                         Analizar la respuesta JSON y obtener las rutas
//                        DirectionsJSONParser parser = new DirectionsJSONParser();
//                        List<List<HashMap<String, String>>> routes = parser.parse(response);
//
//                         Dibujar las rutas en el mapa
//                        for (int i = 0; i < routes.size(); i++) {
//                            List<HashMap<String, String>> path = routes.get(i);
//                            PolylineOptions options = new PolylineOptions();
//
//                            for (int j = 0; j < path.size(); j++) {
//                                HashMap<String, String> point = path.get(j);
//                                double lat = Double.parseDouble(point.get("lat"));
//                                double lng = Double.parseDouble(point.get("lng"));
//                                LatLng position = new LatLng(lat, lng);
//
//                                options.add(position);
//                            }
//
//                            options.color(Color.RED);
//                            options.width(10);
//                            googleMap.addPolyline(options);
//                        }
//                    }
//                }, null);
//
//        mRequestQueue.add(request);
//    }*/
}