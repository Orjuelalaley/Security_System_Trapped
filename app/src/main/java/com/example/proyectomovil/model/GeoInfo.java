package com.example.proyectomovil.model;

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

    public GeoInfo(String title, String content, String imageBase64, Double lat, Double lng) {
        this.title = title;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.lat = lat;
        this.lng = lng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
