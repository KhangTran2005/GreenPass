package com.example.greenpass.data

import android.location.Location
import android.util.Log
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.Geofence
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt

object Database {
    var geofences: MutableList<Geofence>? = null
    lateinit var username: String
    var user: User? = null

    fun addNewUser(username: String, password: String, user: User){
        val base = Firebase.database.reference.child("users").child(username)
        base.child("name").setValue(user.name)
        base.child("age").setValue(user.age)
        base.child("DoB").setValue(user.DoB)
        base.child("Sex").setValue(user.sex)
        base.child("Nationality").setValue(user.nationality)
        base.child("ID").setValue(user.ID)

        base.child("infractions").child("N").setValue(0)
        base.child("ratings").child("N").setValue(0)

        val usernameToPassword = Firebase.database.reference.child("usernameToPassword")
        usernameToPassword.child(username).setValue(BCrypt.hashpw(password, BCrypt.gensalt()))

        Firebase.database.reference
            .child("nameToUsername")
            .child(user.name).setValue(username)

        //did not exist
        base.child("clearance_level").setValue(0)
        base.child("vaccination_date").setValue("Not Vaccinated")
        base.child("vaccination_place").setValue("Go to your nearest approved clinic for vaccination")
    }

    fun updateUser(){
        Firebase.database.reference.child("users").child(username).get()
            .addOnSuccessListener { snapshot ->
                user?.let { user ->
                    user.vacc_date = snapshot.child("vaccination_date").value.toString()
                    user.vacc_loc = snapshot.child("vaccination_place").value.toString()
                    Clearance.findByValue(snapshot.child("clearance_level").value.toString().toInt())?.let{
                        user.clearance = it
                    }
                }
            }
    }

    fun writeLocation(loc: Location){
        val lat = loc.latitude
        val long = loc.longitude
        println("writing to database")
        Firebase.database.reference
            .child("users")
            .child(username)
            .child("loc")
            .child("lat")
            .setValue(lat).addOnSuccessListener {
            Log.i("LocationService","Updated firebase")
        }.addOnFailureListener{
            it.printStackTrace()
        }
        Firebase.database.reference
            .child("users")
            .child(username)
            .child("loc")
            .child("long")
            .setValue(long)
        Log.i("LocationService","Changed latitude to $lat")
        Log.i("LocationService","Changed longitude to $long")
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