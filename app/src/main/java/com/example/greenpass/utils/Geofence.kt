package com.example.greenpass.utils

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

data class Geofence (
    var name: String,
    val marker: Marker,
    val circle: Circle,
    val clearance: Clearance
){
    override fun toString(): String {
        return listOf(name,marker.position.latitude,marker.position.longitude,circle.radius).joinToString(",")
    }
    companion object{
        fun readFromString(str: String, map: GoogleMap) : Geofence{
            val tmp = str.split(",")
            val latLng: LatLng = LatLng(tmp[1].toDouble(),tmp[2].toDouble())
            return Geofence(
                tmp[0],
                map.addMarker(
                    MarkerOptions()
                    .position(latLng)
                    .title(tmp[0]))!!,
                map.addCircle(
                    CircleOptions()
                    .radius(tmp[3].toDouble())
                    .center(latLng)
                    .fillColor(Color.argb(100, 150,150,150))
                    .strokeColor(Color.TRANSPARENT))!!,
                Clearance.valueOf(tmp[-1])
            )
        }
    }
}
