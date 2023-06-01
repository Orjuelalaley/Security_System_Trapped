package com.example.proyectomovil.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.proyectomovil.R;

public class DevicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_devices);

        LottieAnimationView animationView = findViewById(R.id.animation_view);
        Switch movimientoSalaSwitch = findViewById(R.id.sensor_movimiento_sala_switch);
        Switch movimientoPatioSwitch = findViewById(R.id.sensor_movimiento_sala_switch2);
        Switch movimientoCasaSwitch = findViewById(R.id.sensor_movimiento_sala_switch3);
        Button camaraSalaButton = findViewById(R.id.button_camara_sala);

        // Configura la animación
        animationView.setAnimation(R.raw.sensor);
        animationView.playAnimation();

        // Aquí puedes agregar el código para manejar los eventos de los componentes
        // Por ejemplo, agregar listeners a los switches o botón y definir su comportamiento

    }
}
