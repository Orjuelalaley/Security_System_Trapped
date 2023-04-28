package com.example.proyectomovil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.proyectomovil.R;
import com.example.proyectomovil.databinding.ActivityRegisterBinding;
import com.example.proyectomovil.model.User;
import com.example.proyectomovil.utils.AlertUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends BasicActivity {
    private ActivityRegisterBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        binding.RegBtn.setOnClickListener(v -> register());

    }

    private void register() {
        if(Objects.requireNonNull(binding.registerFullName.getEditText()).getText().toString().isEmpty()) {
            binding.registerFullName.setError(getString(R.string.error_name_label));
            return;
        } else binding.registerFullName.setErrorEnabled(false);

        if (Objects.requireNonNull(binding.registerPhone.getEditText()).getText().toString().isEmpty()) {
            binding.registerPhone.setError(getString(R.string.error_phone_label));
            return;
        } else binding.registerPhone.setErrorEnabled(false);
        if (Objects.requireNonNull(binding.registerEmail.getEditText()).getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.getEditText().getText().toString()).matches()){
            binding.registerEmail.setError(getString(R.string.mail_error_label));
            return;
        } else binding.registerEmail.setErrorEnabled(false);
        if (Objects.requireNonNull(binding.registerCedula.getEditText()).getText().toString().isEmpty()) {
            binding.registerCedula.setError(getString(R.string.error_cedula_label));
            return;
        } else binding.registerCedula.setErrorEnabled(false);
        if (Objects.requireNonNull(binding.registerPass.getEditText()).getText().toString().isEmpty()) {
            binding.registerPass.setError(getString(R.string.error_pass_label));
            return;
        } else binding.registerPass.setErrorEnabled(false);
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        binding.RegBtn.setVisibility(View.GONE);
        String name = binding.registerFullName.getEditText().getText().toString();
        String email = binding.registerEmail.getEditText().getText().toString();
        long phone = Long.parseLong(binding.registerPhone.getEditText().getText().toString());
        String password = binding.registerPass.getEditText().getText().toString();
        User user = new User(name, email, phone, password);

        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.cancel();
                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                binding.brandLogo.setVisibility(View.GONE);
                binding.animationView.setVisibility(View.VISIBLE);
                binding.animationView.resumeAnimation();
                binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        db.collection("Users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).set(user);
                        finish();  // finish the activity
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {
                    }
                });
            } else {
                alertsHelper.indefiniteSnackbar(binding.getRoot(), Objects.requireNonNull(task.getException()).getLocalizedMessage());
                progressDialog.cancel();
                binding.RegBtn.setVisibility(View.VISIBLE);
            }
        });
    }
}