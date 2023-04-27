package com.example.proyectomovil.model;

import lombok.Data;

@Data
public class User {
    private String name;
    private String email;
    private long phone;
    private String password;

    public User(String name, String email, long phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
