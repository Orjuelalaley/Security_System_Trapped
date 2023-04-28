package com.example.proyectomovil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.ActivityAddDeviceBinding;

public class AddDeviceActivity extends AppCompatActivity {

    private ActivityAddDeviceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        binding = ActivityAddDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}