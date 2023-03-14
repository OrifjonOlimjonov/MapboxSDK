package uz.orifjon.mapboxexample

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.core.location.*
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.*
import com.mapbox.maps.plugin.scalebar.scalebar
import uz.orifjon.mapboxexample.databinding.ActivityMainBinding
import uz.orifjon.mapboxexample.service.LocationService
import uz.orifjon.mapboxexample.utils.LocationPermissionHelper
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity()  {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))

        locationPermissionHelper.checkPermissions {
            Intent(applicationContext,LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
            onMapReady()
        }



    }
    private fun onMapReady() {
        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )

        disabledScaleAndCompass()

        configureTheme()
    }

    private fun configureTheme() {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
            {
                binding.mapView.getMapboxMap().loadStyleUri(
                    Style.DARK
                ) {
                    initLocationComponent()
                }
            }
            Configuration.UI_MODE_NIGHT_NO->{
                binding.mapView.getMapboxMap().loadStyleUri(
                    Style.LIGHT
                ) {
                    initLocationComponent()
                }
            }
        }
    }

    private fun disabledScaleAndCompass() {
        binding.mapView.scalebar.enabled = false
        binding.mapView.compass.enabled = false
    }


    private fun initLocationComponent() {
        val locationComponentPlugin = binding.mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.yellow,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.yellow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(2.0)
                    }
                }.toJson()
            )
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




}