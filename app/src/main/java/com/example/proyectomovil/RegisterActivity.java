package com.example.proyectomovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import com.example.proyectomovil.databinding.ActivityRegisterBinding;
import com.example.proyectomovil.utils.AlertUtils;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.RegBtn.setOnClickListener(view -> {
            String username = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (!username.equals("") && !password.equals("")) {
                // Si los credentials son válidos, muestra un mensaje e ingresa al sistema
                AlertUtils.shortToast(this, "Bienvenido " + username);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Si los credenciales no son válidos, muestra un mensaje de error
                AlertUtils.shortToast(this, "Credenciales incorrectas o imcompletas");
            }
        });
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.RegBtn.performClick();
                return true;
            }
            return false;
        });
    }
}