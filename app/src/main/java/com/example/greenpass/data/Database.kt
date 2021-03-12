package com.example.greenpass.data

import android.location.Location
import android.util.Log
import com.example.greenpass.ui.geofence.GeofenceFragment
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.Geofence
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import kotlin.random.Random

class Database(
    val userId: String,
) {
    val userInfoDatabase = Firebase.database.reference.child("users").child(userId)
    fun writeLocation(loc: Location){
        val lat = loc.latitude
        val long = loc.longitude
        println("writing to database")
        userInfoDatabase.child("latitude").setValue(lat).addOnSuccessListener {
            println("Updated firebase")
        }.addOnFailureListener{
            println(it.printStackTrace())
        }
        userInfoDatabase.child("longitude").setValue(long)
        Log.i("latitude update","Changed latitude to $lat")
        Log.i("longitude update","Changed longitude to $long")
    }

    companion object{
        var geofences: MutableList<Geofence>? = null

        fun fetchGeofences(mMap: GoogleMap, N: Int = -1) {
            if(N == -1){
                Firebase.database.reference
                    .child("geofences")
                    .child("N").get().addOnSuccessListener {
                        fetchGeofences(mMap, it.value.toString().toInt())
                    }
            } else {
                geofences = mutableListOf()
                GeofenceFragment.markers = mutableListOf()
                for (i in 0 until N) {
                    Firebase.database.reference
                            .child("geofences")
                            .child(i.toString()).get().addOnSuccessListener {
                                val name = it.child("name").value.toString()
                                val lat = it.child("lat").value.toString().toDouble()
                                val long = it.child("long").value.toString().toDouble()
                                val radius = it.child("radius").value.toString().toDouble()
                                val clearance = it.child("clearance").value.toString().toInt()
                                geofences!!.add(Geofence.addGeofenceToMap(mMap,name,lat,long,radius,clearance))
                            }
                }
            }
        }
        fun addGeofencesToMap(mMap: GoogleMap,clearance: Clearance = Clearance.ANY){
            if(geofences != null){
                geofences!!
                    .filter {
                        clearance <= it.clearance
                    }.forEach{
                                Geofence.addGeofenceToMap(mMap,it.name,it.marker.position.latitude,
                                        it.marker.position.longitude,it.circle.radius,it.clearance.ordinal)
                    }
            } else{
                fetchGeofences(mMap)
            }
        }
    }
}