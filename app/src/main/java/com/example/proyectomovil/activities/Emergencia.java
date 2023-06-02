package com.example.proyectomovil.activities;


public class Emergencia {
    private String emergenciaId;
    private String tipoEmergencia;

    public Emergencia() {
        // Constructor vacío necesario para Firebase
    }

    public Emergencia(String emergenciaId, String tipoEmergencia) {
        this.emergenciaId = emergenciaId;
        this.tipoEmergencia = tipoEmergencia;
    }

    public String getEmergenciaId() {
        return emergenciaId;
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }
}
