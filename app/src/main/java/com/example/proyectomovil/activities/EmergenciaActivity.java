package com.example.proyectomovil.activities;




import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectomovil.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergenciaActivity extends AppCompatActivity {

    private Button buttonPolice;
    private Button buttonFirefighters;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencia);

        // Obtener referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonPolice = findViewById(R.id.buttonPolice);
        buttonFirefighters = findViewById(R.id.buttonFirefighters);

        buttonPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificarEmergencia("Policía");
                mostrarToast("Se notificó emergencia a la Policía");

            }
        });

        buttonFirefighters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificarEmergencia("Bomberos");
                mostrarToast("Se notificó emergencia a los Bomberos");
            }
        });
    }
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }


    private void notificarEmergencia(String tipoEmergencia) {
        // Generar un nuevo ID único para la emergencia
        String emergenciaId = databaseReference.child("emergencias").push().getKey();

        // Guardar la emergencia en la base de datos de Firebase
        databaseReference.child("emergencias").child(emergenciaId).setValue(tipoEmergencia);
    }
}
