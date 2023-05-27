package com.example.proyectomovil.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.ActivityLogInBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class LoginActivity extends BasicActivity {

    private ActivityLogInBinding binding;
    FirebaseApp firebaseApp;
    private static final int TIME_INTERVAL = 2000; // Intervalo de tiempo entre pulsaciones en milisegundos
    private long mBackPressed;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseApp = FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);
        binding.loginButton.setOnClickListener(v -> doLogin());
        binding.signupButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void doLogin() {
        if(Objects.requireNonNull(binding.loginEmail.getEditText()).getText().toString().isEmpty() ) {
            binding.loginEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.loginEmail.setErrorEnabled(false);
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.loginEmail.getEditText().getText().toString()).matches()){
            binding.loginEmail.setError(getString(R.string.error_email_label));
            return;
        } else binding.loginEmail.setErrorEnabled(false);
        if (Objects.requireNonNull(binding.loginPassword.getEditText()).getText().toString().isEmpty()) {
            binding.loginPassword.setError(getString(R.string.error_pass_label));
            return;
        } else binding.loginPassword.setErrorEnabled(false);
        String email = binding.loginEmail.getEditText().getText().toString();
        String password = binding.loginPassword.getEditText().getText().toString();
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            progressDialog.cancel();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            progressDialog.cancel();
            alertsHelper.indefiniteSnackbar(binding.getRoot(), "Usuario o contraseña incorrectos");
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