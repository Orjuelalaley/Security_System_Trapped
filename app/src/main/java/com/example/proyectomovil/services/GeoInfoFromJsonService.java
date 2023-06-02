package com.example.proyectomovil.services;

import android.content.Context;

import com.example.proyectomovil.model.GeoInfo;


import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ActivityComponent.class)
public class GeoInfoFromJsonService {
    public static final String TAG = GeoInfoFromJsonService.class.getName();
    private final ArrayList<GeoInfo> geoInfoList = new ArrayList<>();

    @Inject
    public GeoInfoFromJsonService(@ApplicationContext Context context) {
        //loadGeoInfoFromJson();
    }

    public ArrayList<GeoInfo> getGeoInfoList() {
        return geoInfoList;
    }
}
