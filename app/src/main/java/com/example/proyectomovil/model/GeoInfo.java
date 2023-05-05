package com.example.proyectomovil.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class GeoInfo {
    private String title;
    private String content;
    private String imageBase64;
    private Double lat;
    private Double lng;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
