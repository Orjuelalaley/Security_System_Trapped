package com.example.proyectomovil.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.FragmentHomeBinding;
import com.example.proyectomovil.services.GeoInfoFromJsonService;
import com.example.proyectomovil.services.GeocoderService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.Scanner;

import javax.inject.Inject;


public class HomeFragment extends Fragment implements SensorEventListener, MapEventsReceiver{
    @Inject
    GeoInfoFromJsonService geoInfoFromJsonService;
    @Inject
    GeocoderService geocoderService;

    Double currentUserLatitude = 0d, currentUserLongitude = 0d;

    public FragmentHomeBinding binding;

    private Marker marker;

    static final Double INITIAL_ZOOM_LEVEL = 18.0;

    static final Double EARTH_RADIUS = 6371.0;

    private final LatLng UNIVERSIDAD = new LatLng(4.628150, -74.064227);

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TelephonyManager telephonyManager;

    private boolean isPhoneCalling = false;

    private Marker selectedMarker;
    GeoPoint startPoint;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        binding.map.setMultiTouchControls(true);
        binding.map.getController().setZoom(INITIAL_ZOOM_LEVEL);
        IMapController mapController = binding.map.getController();
        // Obtén el proveedor de ubicación actual
        readJson();
        binding.map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && selectedMarker != null) {
                    GeoPoint endPoint = selectedMarker.getPosition();
                    double distance = startPoint.distanceToAsDouble(endPoint) / 1000;
                    Toast.makeText(getContext(), "Distancia al marcador: " + distance + " km", Toast.LENGTH_LONG).show();
                    return true; // Para indicar que el evento ha sido consumido y no se procese por defecto
                }
                return false;
            }
        });

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        // Verifica si tienes permiso para acceder a la ubicación
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Obtiene la última ubicación conocida
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null) {
                // Mueve el centro del mapa a tu ubicación actual
                startPoint = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                binding.map.getController().setCenter(startPoint);
                // Crea un marcador en tu ubicación actual
               AddMarker(startPoint , "Ubicación actual");
            }
        }
        // CardView de Dispositivos
        binding.dispositivosCardView.setOnClickListener(v -> {
            // Iniciar la actividad de Dispositivos
            startActivity(new Intent(getContext(), DevicesActivity.class));
        });

        // CardView de Habitación
        binding.habitacionCardView.setOnClickListener(v -> {
            // Navegar hacia la actividad de Habitación
            startActivity(new Intent(getContext(), CamaraHabitacionActivity.class));
        });
        // CardView de Habitación
        binding.emergenciaCardView.setOnClickListener(v -> {
            // Navegar hacia la actividad de Habitación
            startActivity(new Intent(getContext(), EmergenciaActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void AddMarker(GeoPoint startPoint, String title) {
        Marker marker = new Marker(binding.map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_marker_blue));
        binding.map.getOverlays().add(marker);
    }

    private void readJson() {
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.locations));
        StringBuilder builder = new StringBuilder();
        while (sc.hasNextLine()) builder.append(sc.nextLine());
        parseJson(builder.toString());
    }

    private Marker createMarker(GeoPoint p, String title, int iconID) {
        org.osmdroid.views.overlay.Marker marker = null;
        marker = new org.osmdroid.views.overlay.Marker(binding.map);
        if (title != null) marker.setTitle(title);
        marker.setSubDescription("");
        marker.setPosition(p);
        marker.setIcon(getResources().getDrawable(iconID));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        return marker;
    }

    private void placeMarker(Marker marker) {
        binding.map.getOverlays().add(marker);
    }

    private void parseJson(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray locations = root.getJSONArray("locationsArray");
            for (int i = 0; i < locations.length(); i++) {
                Marker marker = createMarker(new GeoPoint(Double.parseDouble(locations.getJSONObject(i).getString("latitude")), Double.parseDouble(locations.getJSONObject(i).getString("longitude"))), locations.getJSONObject(i).getString("name"), R.drawable.ic_marker_red);
                placeMarker(marker);
            }

        } catch (Exception e) {
            Log.e("ERROR", "There was an error");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightLevel = event.values[0];
        if (event.values[0] < 3) {
            binding.map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
        } else {
            binding.map.getOverlayManager().getTilesOverlay().setColorFilter(null);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        if (selectedMarker != null) {
            selectedMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker_red));
        }
        selectedMarker = createMarker(p, "Ubicación Seleccionada", R.drawable.ic_marker_blue);
        selectedMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker_green));
        placeMarker(selectedMarker);
        return true;
    }
}