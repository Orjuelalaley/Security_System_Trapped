package com.example.proyectomovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectomovil.databinding.ActivityLogInBinding;
import com.example.proyectomovil.utils.*;


public class LoginActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private static final int TIME_INTERVAL = 2000; // Intervalo de tiempo entre pulsaciones en milisegundos
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.loginBtn.setOnClickListener(view -> {
            String username = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            if (username.equals("usuario") && password.equals("1234")) {
                // Si los credenciales son válidos, muestra un mensaje y haz algo
                AlertUtils.shortToast(this, "Bienvenido " + username);
                // Aquí puedes navegar a otra actividad o hacer otra cosa
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
                binding.loginBtn.performClick();
                return true;
            }
            return false;
        });
        binding.registratonBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

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
}