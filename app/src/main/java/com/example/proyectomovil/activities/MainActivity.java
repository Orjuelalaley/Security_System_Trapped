package com.example.proyectomovil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int TIME_INTERVAL = 2000; // Intervalo de tiempo entre pulsaciones en milisegundos
    private long mBackPressed;
    FirebaseApp inicar = FirebaseApp.initializeApp(this);
    private DatabaseReference reference;

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            binding.navView.setCheckedItem(R.id.nav_home);
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Obtiene el valor de la referencia de la base de datos
                String value = snapshot.getValue(String.class);

                // Si el valor es diferente de null
                if (value != null) {
                    // Si el valor es igual a 1
                    if (value.equals("1")) {
                        Toast.makeText(MainActivity.this, "El dispositvo detecto movimiento. ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            } else if (item.getItemId() == R.id.settings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
            } else if (item.getItemId() == R.id.homeButton) {
                showBottomDialog();
            } else if (item.getItemId() == R.id.dispositivos) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DevicesFragment()).commit();
            } else if (item.getItemId() == R.id.about) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HelpFragment()).commit();
            }
                    return true;
                }
        );
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
        } else if (item.getItemId() == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShareFragment()).commit();
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
}