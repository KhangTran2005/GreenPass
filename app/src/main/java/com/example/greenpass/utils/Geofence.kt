package com.example.greenpass.utils

import android.graphics.Color
import com.example.greenpass.ui.geofence.GeofenceFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlin.math.ln

data class Geofence (
    var name: String,
    val circle: Circle,
    val clearance: Clearance
){
    companion object{
        fun addGeofenceToMap(map: GoogleMap, name:String, lat: Double, long: Double, radius: Double, clearance: Int) : Geofence{
            val latLng = LatLng(lat, long)
            return Geofence(
                    name,
                    map.addCircle(CircleOptions()
                            .radius(radius)
                            .center(latLng)
                            .fillColor(Color.argb(40,255,0,0))
                            .strokeColor(Color.TRANSPARENT)),
                    Clearance.findByValue(clearance) ?: Clearance.ANY
            )
        }

    }
}
