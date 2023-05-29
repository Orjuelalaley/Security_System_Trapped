package com.example.proyectomovil.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.FragmentHomeBinding;
import com.example.proyectomovil.services.GeoInfoFromJsonService;
import com.example.proyectomovil.services.GeocoderService;
import com.example.proyectomovil.services.LocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.Scanner;

import javax.inject.Inject;


public class HomeFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    @Inject
    GeoInfoFromJsonService geoInfoFromJsonService;

    @Inject
    GeocoderService geocoderService;

    @Inject
    LocationService locationService;

    public FragmentHomeBinding binding;

    private LocationRequest locationRequest;

    private Marker marker;

    static final Double INITIAL_ZOOM_LEVEL = 14.0;

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
        Context context = view.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Configuration.getInstance().load(context, preferences);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        binding.map.setMultiTouchControls(true);
        binding.map.getController().setZoom(INITIAL_ZOOM_LEVEL);
        locationRequest = createLocationRequest();
    }

    private LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void readJson() {
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.locations));
        StringBuilder builder = new StringBuilder();
        while (sc.hasNextLine())
            builder.append(sc.nextLine());
        parseJson(builder.toString());
    }

    private Marker createMarker(GeoPoint p, String title, int iconID) {
        org.osmdroid.views.overlay.Marker marker = null;
        marker = new org.osmdroid.views.overlay.Marker(binding.map);
        if (title != null) marker.setTitle(title);
        marker.setSubDescription("");
        marker.setPosition(p);
        marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);
        return marker;
    }
    private void placeMarker(org.osmdroid.views.overlay.Marker marker) {
        binding.map.getOverlays().add(marker);
    }

    private void parseJson(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray locations = root.getJSONArray("locationsArray");
            for (int i = 0; i < locations.length(); i++) {
                org.osmdroid.views.overlay.Marker marker = createMarker(new GeoPoint(Double.parseDouble(locations.getJSONObject(i).getString("latitude")),
                                Double.parseDouble(locations.getJSONObject(i).getString("longitude"))),
                        locations.getJSONObject(i).getString("name"),
                        R.drawable.ic_marker_red);
                placeMarker(marker);
            }
        } catch (Exception e) {
            Log.e("ERROR", "There was an error");
        }
    }


//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Configurar opciones del mapa
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));
//        googleMap.getUiSettings().setAllGesturesEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setZoomGesturesEnabled(true);
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.getUiSettings().isMyLocationButtonEnabled();
//        // Mover y marcar una ubicación en el mapa
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UNIVERSIDAD, INITIAL_ZOOM_LEVEL));
//        mMap.addMarker(new MarkerOptions().position(UNIVERSIDAD)
//                .icon(BitmapUtils.getBitmapDescriptor(getContext(), R.drawable.baseline_person_pin_circle_24))
//                .title("Universidad")
//                .anchor(0.5f, 1.0f)
//                .zIndex(1.0f));
//        googleMap.setOnMapLongClickListener(latLng -> {
//            if (marker != null) {
//                marker.remove();
//            }
//            MarkerOptions markerOption = new MarkerOptions();
//            markerOption.position(latLng)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Ubicación seleccionada")
//                    .snippet(latLng.toString());
//            marker = googleMap.addMarker(markerOption);
//            //dibujarRutas(userPosition.getPosition(),marker.getPosition());
//        });
//    }

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