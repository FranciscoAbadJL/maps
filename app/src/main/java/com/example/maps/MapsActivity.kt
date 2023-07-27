package com.example.maps
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.maps.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode


private const val LOCATION_PERMISSION_REQUEST_CODE = 2000
private const val DEFAULT_MAP_SCALE = 13.0f

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val rosticeriasmtz = mutableListOf<Rosticerias>()
    private lateinit var pollo: BitmapDescriptor
    private val userLocation = Location("")
    var poly: Polyline? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            startActivity(Intent(this, ListaRos::class.java))
        }

        rosticeriasmtz.add(Rosticerias("ROSTICERIA DE POLLOS LUPITA", 20.06394745, -97.05683784))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA DE POLLOS ROSTICERIA DEIVY", 20.06272211, -97.05701326))
        rosticeriasmtz.add(Rosticerias("ROSTICERÍA EL ASADOR", 20.12947963, -96.99445532))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL FOGON", 20.06119051, -97.05798803))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL FOGON", 20.07046022, -97.06280869))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PESCADOR", 20.05843286, -97.05291111))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL POLLO BRONCO", 20.06280747, -97.05328847))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL POLLON", 20.06772488, -97.04767304))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PUENTE", 20.05877158, -97.05343994))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PUENTE", 20.06559456, -97.04984646))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA LLAMARADA", 20.07420713, -97.06300038))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA LLAMARADA", 20.065629, -97.0494147))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA PALAPA NORTEÑA", 20.06274651, -97.05572105))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA PALAPA NORTEÑA", 20.07395274, -97.06263125))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LUPITA", 20.07299227, -97.0645257))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA MR POLLO", 20.06130814, -97.05714042))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA PIPIOLOS", 20.06400393, -97.05648428))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA POLLOS A LA LEÑA", 20.06433767, -97.05681565))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA POLLOS PIPIOLO", 20.1292851, -96.99620022))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA RAMOS", 20.06337913, -97.0571883))


        pollo = getTacoIcon()

        checkLocationPermission()
    }


    private fun checkLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                userLocation.latitude = location.latitude
                userLocation.longitude = location.longitude
                setupMap()
            }
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showLocationPermissionRationaleDialog()
            } else {
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showLocationPermissionRationaleDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.need_location_permission_dialog_title)
            .setMessage(R.string.need_location_permission_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }.setNegativeButton(R.string.no) { _, _ ->
                finish()
            }
        dialog.show()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val userLatLng = LatLng(userLocation.latitude, userLocation.longitude)
        val userMarker = MarkerOptions().position(userLatLng)
        mMap.addMarker(userMarker)

        for (taqueria in rosticeriasmtz) {
            val tacoPosition = LatLng(taqueria.latitud, taqueria.longitude)
            val tacoLocation = Location("")

            tacoLocation.latitude = taqueria.latitud
            tacoLocation.longitude = taqueria.longitude

            val distanceToTaco = tacoLocation.distanceTo(userLocation)

            val tacoMarkerOptions = MarkerOptions()
                .icon(pollo)
                .position(tacoPosition)
                .title(taqueria.name)
                .snippet(getString(R.string.distance_to_format, distanceToTaco))
            mMap.addMarker(tacoMarkerOptions)
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_MAP_SCALE))

        mMap.setOnMarkerClickListener { marker ->

            for (rosticeriasmtz in rosticeriasmtz) {
                val taquerias_DrinksLatLng = LatLng(rosticeriasmtz.latitud, rosticeriasmtz.longitude)
                if (marker.position == taquerias_DrinksLatLng) {
                    val selectedtaquerias_Drinks = rosticeriasmtz
                    val destination = LatLng(selectedtaquerias_Drinks.latitud, selectedtaquerias_Drinks.longitude)
                    getDirectionsToLocation(destination)
                    break
                }
            }
            false
        }
    }

    private fun getTacoIcon(): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_chicken)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(drawable?.intrinsicWidth ?: 0,
            drawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable?.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    private fun getDirectionsToLocation(destination: LatLng) {
        val origin = "${userLocation.latitude},${userLocation.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val context = GeoApiContext.Builder()
                    .apiKey("AIzaSyDszbOKKZ47FSqlnD_fYPzy_WfmUz0cyJU")
                    .build()
                val directionsResult = DirectionsApi.newRequest(context)
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destinationString)
                    .await()
                if (directionsResult.routes.isNotEmpty()) {
                    val route = directionsResult.routes[0]
                    val points = route.overviewPolyline.decodePath()

                    val polylineOptions = PolylineOptions()
                        .addAll(points.map { LatLng(it.lat, it.lng) })
                        .color(Color.BLUE)
                        .width(5f)
                    if (poly != null) {
                        poly!!.remove()
                    }
                    poly = mMap.addPolyline(polylineOptions)
                } else {
                    Toast.makeText(this@MapsActivity, "No se encontró una ruta", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MapsActivity, "Error al obtener las direcciones: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(ContentValues.TAG,"Error al obtener las direcciones: ${e.message}")
            }
        }
    }
}