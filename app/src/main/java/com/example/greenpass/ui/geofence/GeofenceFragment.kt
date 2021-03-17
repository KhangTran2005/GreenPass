package com.example.greenpass.ui.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.utils.Clearance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mancj.slideup.SlideUp
import com.mancj.slideup.SlideUpBuilder
import kotlinx.android.synthetic.main.fragment_geofence.*

class GeofenceFragment : Fragment(), GoogleMap.OnMarkerClickListener , GoogleMap.OnPoiClickListener{
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val viewModel: GeofenceViewModel by lazy {
        ViewModelProvider(this).get(GeofenceViewModel::class.java)
    }
    private lateinit var slideUp: SlideUp
    private lateinit var lastPOI: PointOfInterest
    private lateinit var placesClient: PlacesClient


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        googleMap.setLatLngBoundsForCameraTarget(SINGAPORE_BOUNDS)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.activity as Activity)
        getLocationPermission()
        if(!locationPermissionGranted || (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)){
            Toast.makeText(this.context,"Locations permissions not granted",Toast.LENGTH_SHORT).show()
        } else {
            googleMap.isMyLocationEnabled = true
            getDeviceLocation()
        }

        googleMap.setOnPoiClickListener(this)

        Firebase.database.reference
                .child("users")
                .child(Database.username)
                .child("clearance_level").get().addOnSuccessListener {
                    Database.addGeofencesToMap(googleMap, Clearance.findByValue(it.value.toString().toInt())
                            ?: Clearance.ANY)
                }

//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_geofence, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_sign_out).isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        slideUp = SlideUpBuilder(geofence_dialog_box)
            .withStartState(SlideUp.State.HIDDEN)
            .withStartGravity(Gravity.BOTTOM)
            .build()
//        if(viewModel.isDialogOpen.value == true){
//            slideUp.show()
//        }

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            println("Moving camera")
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun onRatingBarChangeListener(rating:Double) {
        Firebase.database.reference
                .child("users")
                .child(Database.username)
                .child("ratings")
                .get().addOnSuccessListener { ratings ->
                    if(!ratings.child(lastPOI.placeId).exists()) {
                        ratings.child(lastPOI.placeId).ref.setValue(0.0).addOnSuccessListener {
                            onRatingBarChangeListener(rating)
                        }
                    } else{
                        ratings.child(lastPOI.placeId).ref.get().addOnSuccessListener {
                            val prevRating = (it.value as Number).toDouble()
                            Log.i(TAG,"Previous rating: $prevRating")
                            Log.i(TAG,"Current rating: $rating")
                            ratings.child(lastPOI.placeId).ref.setValue(rating)

                            fun updatePlaceRating() {
                                Firebase.database.reference
                                        .child("places")
                                        .child(lastPOI.placeId)
                                        .child("rating")
                                        .get().addOnSuccessListener { placeRating ->
                                            if (!placeRating.exists()) {
                                                placeRating.child("N")
                                                        .ref
                                                        .setValue(0)
                                                        .addOnSuccessListener {
                                                            placeRating.child("sum")
                                                                    .ref
                                                                    .setValue(0.0)
                                                            updatePlaceRating()
                                                        }
                                            } else{
                                                val N: Int = (placeRating.child("N").value as Number).toInt()
                                                val sum = (placeRating.child("sum").value as Number).toDouble()
                                                if (prevRating <= 0.0 + 1e-9 && rating > 0.0) {
                                                    // Previously unrated to rated
                                                    placeRating.child("N")
                                                            .ref
                                                            .setValue(N + 1)
                                                    placeRating.child("sum")
                                                            .ref
                                                            .setValue(sum + rating)
                                                } else if (prevRating > 0.0 && rating <= 0.0 + 1e-9) {
                                                    //Rated to unrated
                                                    placeRating.child("N")
                                                            .ref
                                                            .setValue(N - 1)
                                                    placeRating.child("sum")
                                                            .ref
                                                            .setValue(sum - rating)
                                                } else{
                                                    placeRating.child("sum")
                                                            .ref
                                                            .setValue(sum - prevRating + rating)
                                                }
                                            }
                                            }
                                        }
                            updatePlaceRating()
                        }

                    }
                }
    }


    override fun onPoiClick(poi: PointOfInterest){
        Log.i(TAG,"Registered POI Click")
        lastPOI=poi
        viewModel.adjustDialog(slideUp,true)
        if(context==null){
            Log.e(TAG,"Can't get username as context is null")
            return
        }

        poi_nameField.text = poi.name.replace("\n"," ")

        Firebase.database.reference
                .child("places")
                .child(lastPOI.placeId)
                .child("rating")
                .get().addOnSuccessListener { placeRating ->
                    if (!placeRating.exists()) {
                        ave_rating_field.text = "0.0"
                        rating_no_field.text = "0"
                    } else {
                        val N = (placeRating.child("N")
                                .value as Number).toInt()
                        val sum = (placeRating.child("sum")
                                .value as Number).toDouble()
                        val ave = if (N == 0) 0.0 else sum / N
                        ave_rating_field.text = "%.2f".format(ave)
                        rating_no_field.text = "$N"
                    }
                }

        poi_ratingBar.setOnRatingBarChangeListener {_,rating,_ ->
            onRatingBarChangeListener(rating.toDouble())
        }

        Firebase.database.reference
                .child("users")
                .child(Database.username)
                .child("ratings")
                .get().addOnSuccessListener {
                    if(it.child(lastPOI.placeId).exists()) {
                        poi_ratingBar.rating = (it.child(lastPOI.placeId).value as Number).toFloat()
                    }else{
                        poi_ratingBar.rating = 0.0f
                    }
                }

        mMap?.animateCamera(CameraUpdateFactory.newLatLng(poi.latLng))
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        var mMap: GoogleMap? = null
        private const val DEFAULT_ZOOM = 15
        private val TAG = GeofenceFragment::class.java.simpleName
        private val SINGAPORE_BOUNDS = LatLngBounds(
            LatLng(1.103883, 103.455741),
            LatLng(1.787399, 104.373282))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        Toast.makeText(context as Context,"vreievivn",Toast.LENGTH_SHORT).show()
        return false
    }
}