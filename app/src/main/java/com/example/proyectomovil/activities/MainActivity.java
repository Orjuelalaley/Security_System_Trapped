package com.example.proyectomovil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.ActivityMainBinding;
import com.example.proyectomovil.services.PermissionService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MainActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private ActivityMainBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int TIME_INTERVAL = 2000; // Intervalo de tiempo entre pulsaciones en milisegundos
    private long mBackPressed;
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;

    @Inject
    PermissionService permissionService;

    private Toast currentToast;
    private DatabaseReference sensorReference;
    private ValueEventListener sensorValueEventListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = binding.drawerLayout;
        binding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference().child("Sensores").child("estado");
        listerToSensor(sensorRef);
    }
    private void listerToSensor(DatabaseReference sensorRef) {
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String sensor = Objects.requireNonNull(snapshot.getValue()).toString();
                    if (sensor.equals("1")){
                        Toast.makeText(MainActivity.this, "El sensor del hogar a detectado un intruso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:911"));
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            if (x<-10){
                currentToast = Toast.makeText(this, "Llamando a emergencias", Toast.LENGTH_SHORT);
                currentToast.show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar esto en este caso
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            } else if (item.getItemId() == R.id.settings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Setting()).commit();
            } else if (item.getItemId() == R.id.homeButton) {
                showBottomDialog();
            } else if (item.getItemId() == R.id.about) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HelpFragment()).commit();
            }
            return true;
        });

        // Cargar el HomeFragment al iniciar la aplicación
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        binding.bottomNavigationView.setSelectedItemId(R.id.home);

        sensorValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer estado = snapshot.getValue(Integer.class);

                if (estado != null && estado == 1) {
                    currentToast = Toast.makeText(MainActivity.this, "¡Se detectó movimiento!", Toast.LENGTH_SHORT);
                    currentToast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        sensorReference.addValueEventListener(sensorValueEventListener);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Setting()).commit();
        } else if (item.getItemId() == R.id.nav_help) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HelpFragment()).commit();
        } else if (item.getItemId() == R.id.nav_logout) {
            logout();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        dialog.setCancelable(true);
        LinearLayout addDevice = dialog.findViewById(R.id.AddDevice);
        LinearLayout editDevice = dialog.findViewById(R.id.EditDevice);
        LinearLayout deleteDevice = dialog.findViewById(R.id.DeleteDevice);

        addDevice.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, AddDeviceActivity.class);
            startActivity(intent);
        });

        editDevice.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Edita la información del dispositivo de seguridad", Toast.LENGTH_SHORT).show();
        });
        deleteDevice.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Elimina el dispositivo de seguridad", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
        currentToast.cancel();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionService.PERMISSIONS_REQUEST_LOCATION) {
            permissionService.getLocationPermission(this);
        }
    }

    private void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}