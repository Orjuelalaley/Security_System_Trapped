package com.example.proyectomovil.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.proyectomovil.R
import com.example.proyectomovil.databinding.FragmentTomMapBinding
import com.example.proyectomovil.services.LocationService
import com.example.proyectomovil.services.PermissionService
import com.example.proyectomovil.utils.AlertUtils
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.tomtom.sdk.location.GeoLocation
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.location.LocationProvider
import com.tomtom.sdk.location.OnLocationUpdateListener
import com.tomtom.sdk.location.android.AndroidLocationProvider
import com.tomtom.sdk.location.gms.GmsLocationProvider
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.image.ImageFactory.fromResource
import com.tomtom.sdk.map.display.location.LocationMarkerOptions
import com.tomtom.sdk.map.display.marker.MarkerOptions
import com.tomtom.sdk.map.display.style.StyleMode
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.map.display.ui.MapFragment.Companion.newInstance
import com.tomtom.sdk.map.display.ui.MapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class TomMapFragment : Fragment(), MapReadyCallback {
    private val binding: FragmentTomMapBinding? = null

    @Inject
    @JvmField
    var permissionService: PermissionService? = null
    var tomTomMap: TomTomMap? = null
    lateinit var mapFragment: MapFragment

    @Inject
    @JvmField
    var locationService: LocationService? = null

    lateinit var locationProvider: AndroidLocationProvider


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //AlertUtils.longToast(this.context,"on create")
        locationProvider =  AndroidLocationProvider(context = this.requireContext())
        val view = inflater.inflate(R.layout.fragment_tom_map, container, false)

        // Obtener el FragmentManager y la transacción



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*locationService!!.locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val locationProvider: LocationProvider = GmsLocationProvider(context!!)
            }
        }*/
    }
    private fun showUserLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationProvider.enable()

        var onLocationUpdateListener = OnLocationUpdateListener { location ->
            tomTomMap?.moveCamera(CameraOptions(location.position, zoom = 15.0))

        }
        locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
        tomTomMap?.setLocationProvider(locationProvider)
        val mapLocationProvider = tomTomMap?.getLocationProvider()
        val isLocationInVisibleArea = tomTomMap?.isCurrentLocationInMapBoundingBox
        val currentLocation: GeoLocation? = tomTomMap?.currentLocation
        val locationMarkerOptions = LocationMarkerOptions(
                type = LocationMarkerOptions.Type.Chevron
        )

        tomTomMap?.enableLocationMarker(locationMarkerOptions)
    }

    override fun onMapReady(map: TomTomMap) {
        tomTomMap = map


        tomTomMap!!.setStyleMode(StyleMode.DARK)
        val geoPoint = GeoPoint(4.628150, -74.064227)
        val cameraOptions = CameraOptions(geoPoint, 13.0, 1.0, 0.0, 1.0)
        tomTomMap!!.animateCamera(cameraOptions, 3.seconds)
        val markerOptions = MarkerOptions(geoPoint, fromResource(R.drawable.baseline_location_on_24))
        tomTomMap!!.addMarker(markerOptions)
        showUserLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Crear una instancia del mapa y agregarlo al FragmentManager
        val mapOptions = MapOptions("fbbPAaHGqA1G8wtADcVkB8ML4i7ZT8NG")
        val mapFragment = newInstance(mapOptions)
        fragmentTransaction.replace(R.id.map_container, mapFragment)

        // Hacer la transacción
        fragmentTransaction.commit()

        mapFragment.getMapAsync{
            tomTomMap:TomTomMap ->
            val geoPoint1 = GeoPoint(1.0, 1.0)
            val cameraOptions1 = CameraOptions(geoPoint1, 1.0, 1.0, 0.0, 1.0)
            tomTomMap!!.moveCamera(cameraOptions1)// tomTomMap.setStyleMode(StyleMode.DARK)
            this.onMapReady(tomTomMap);
        }



        binding?.seguirUsuarioSwitch?.setOnCheckedChangeListener{_,isChecked ->
            if (!isChecked) {
                AlertUtils.longToast(this.context,"CHECK")
                var onLocationUpdateListener = OnLocationUpdateListener { location ->
                }
                locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
                tomTomMap?.setLocationProvider(locationProvider)
            } else {
                var onLocationUpdateListener = OnLocationUpdateListener { location ->
                    tomTomMap?.moveCamera(CameraOptions(location.position, zoom = 15.0))

                }
                locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
                tomTomMap?.setLocationProvider(locationProvider)
            }
        }

        /*binding?.seguirUsuarioSwitch?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                AlertUtils.longToast(this.context,"CHECK")
                var onLocationUpdateListener = OnLocationUpdateListener { location ->
                }
                locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
                tomTomMap?.setLocationProvider(locationProvider)
            } else {
                var onLocationUpdateListener = OnLocationUpdateListener { location ->
                    tomTomMap?.moveCamera(CameraOptions(location.position, zoom = 15.0))

                }
                locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
                tomTomMap?.setLocationProvider(locationProvider)
            }
        })*/
    }
}