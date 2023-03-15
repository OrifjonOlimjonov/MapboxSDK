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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mapbox.android.core.location.*
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.*
import com.mapbox.maps.plugin.scalebar.scalebar
import uz.orifjon.mapboxexample.databinding.ActivityMainBinding
import uz.orifjon.mapboxexample.models.LocationDatabase
import uz.orifjon.mapboxexample.models.UserLocation
import uz.orifjon.mapboxexample.service.LocationService
import uz.orifjon.mapboxexample.utils.LocationPermissionHelper
import uz.orifjon.mapboxexample.viewmodel.MainViewModel
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    companion object var zoom =  14.0
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var viewModel: MainViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))

        locationPermissionHelper.checkPermissions {

            onMapReady()

            startSaveLocation()

            viewModelSettings()

        }


    }

    private fun startSaveLocation() {

        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }


    }

    private fun onMapReady() {

        cameraOption()

        disabledScaleAndCompass()

        configureTheme()

    }

    private fun viewModelSettings() {
        viewModel.getValue().observe(this
        ) {
            val lastLocation = LocationDatabase.getDatabase(this).locationDao().getLastLocation()
            binding.mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(lastLocation.longitude, lastLocation.latitude))
                    .zoom(zoom)
                    .build()
            )
        }
    }

    private fun cameraOption() {

        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )

        binding.btnZoomPlus.setOnClickListener {
            zoom++
            binding.mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .zoom(zoom)
                    .build()
            )
        }

        binding.btnZoomMinus.setOnClickListener {
            zoom--
            binding.mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .zoom(zoom)
                    .build()
            )
        }

        binding.btnNavigation.setOnClickListener {
            val lastLocation = LocationDatabase.getDatabase(this).locationDao().getLastLocation()
            binding.mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(lastLocation.longitude, lastLocation.latitude))
                    .zoom(zoom)
                    .build()
            )
            viewModel.setValue(lastLocation)
        }
    }


    private fun configureTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.mapView.getMapboxMap().loadStyleUri(
                    Style.DARK
                ) {
                    initLocationComponent()
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
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