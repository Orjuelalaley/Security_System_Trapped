package com.example.proyectomovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.proyectomovil.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    HomeFragment homeFragment=new HomeFragment();

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
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                    break;
                case R.id.Configuración:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
                    break;
                case R.id.homeButton:
                    showBottomDialog();
                    break;
                case R.id.dispositivos:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DevicesFragment()).commit();
                    break;
                case R.id.about:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HelpFragment()).commit();
                    break;
            }
            return true;
        }
        );
    }

    //SIN USO
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShareFragment()).commit();
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HelpFragment()).commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
            Toast.makeText(MainActivity.this,"Edita la información del dispositivo de seguridad",Toast.LENGTH_SHORT).show();
        });
        deleteDevice.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this,"Elimina el dispositivo de seguridad",Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}