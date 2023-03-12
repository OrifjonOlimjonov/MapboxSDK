package uz.orifjon.mapboxexample

import android.animation.TypeEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.core.location.*
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import uz.orifjon.mapboxexample.databinding.ActivityMainBinding
import uz.orifjon.mapboxexample.utils.LocationPermissionHelper
import java.lang.ref.WeakReference


private lateinit var locationPermissionHelper: LocationPermissionHelper

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

            if (!MapboxNavigationApp.isSetup()) {
                MapboxNavigationApp.setup {
                    NavigationOptions.Builder(this)
                        .accessToken("sk.eyJ1Ijoib3JpZmpvbm9saW1qb25vdiIsImEiOiJjbGY1NG9nNnUwN2g1NDFubjhzZDhyZDA3In0.cXmPKqrjEiaKUEOQB5rh0Q")
                        // additional options
                        .build()
                }
            }


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