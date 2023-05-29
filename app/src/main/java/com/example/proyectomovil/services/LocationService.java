package com.example.proyectomovil.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
@Module
@InstallIn(ActivityComponent.class)
public class LocationService {
    public static final String TAG = LocationService.class.getName();
    private final Context context;

    //Location variables
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    public LocationCallback getLocationCallback() {
        return locationCallback;
    }

    LocationCallback locationCallback;

    @Inject
    public LocationService(@ApplicationContext Context context, FragmentActivity activity) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = createLocationRequest();
    }

    protected LocationRequest createLocationRequest() {
        return new LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(5))
                .build();
    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        if (locationCallback != null) {
            Log.d(TAG, "startLocation: Start location updates.");
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            SettingsClient client = LocationServices.getSettingsClient(context);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(context.getMainExecutor(), locationSettingsResponse -> {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            });
        } else {
            Log.e(TAG, "startLocation() returned: locationCallback is null, please define it first.");
        }
    }

    public void stopLocation() {
        Log.d(TAG, "stopLocation: Stopping location updates.");
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }
}
