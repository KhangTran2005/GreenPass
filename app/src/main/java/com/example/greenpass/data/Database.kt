package com.example.greenpass.data

import android.location.Location
import android.util.Log
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.Geofence
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Database {
    var geofences: MutableList<Geofence>? = null
    lateinit var username: String
    var user: User? = null
    fun writeLocation(loc: Location){
        val lat = loc.latitude
        val long = loc.longitude
        println("writing to database")
        Firebase.database.reference
            .child("users")
            .child(username)
            .child("latitude")
            .setValue(lat).addOnSuccessListener {
            println("Updated firebase")
        }.addOnFailureListener{
            println(it.printStackTrace())
        }
        Firebase.database.reference
            .child("users")
            .child(username)
            .child("longitude")
            .setValue(long)
        Log.i("latitude update","Changed latitude to $lat")
        Log.i("longitude update","Changed longitude to $long")
    }

    fun fetchGeofences(mMap: GoogleMap, N: Int = -1) {
        if(N == -1){
            Firebase.database.reference
                .child("geofences")
                .child("N").get().addOnSuccessListener {
                    fetchGeofences(mMap, it.value.toString().toInt())
                }
        } else {
            geofences = mutableListOf()
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
                            Geofence.addGeofenceToMap(mMap,it.name,it.circle.center.latitude,
                                    it.circle.center.longitude,it.circle.radius,it.clearance.ordinal)
                }
        } else{
            fetchGeofences(mMap)
        }
    }
}