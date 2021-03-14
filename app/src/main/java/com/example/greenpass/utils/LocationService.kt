package com.example.greenpass.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.greenpass.data.Database
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.SphericalUtil
import java.time.LocalDateTime

class LocationService : Service() {
    private lateinit var locationManager: LocationManager
    private lateinit var wakeLock: PowerManager.WakeLock
    private var isServiceStarted = false
    private val channelName = "Child Location Service"
    private lateinit var notifManager: NotificationManager
    private var lastLocation: Location? = null

    override fun onBind(intent: Intent?): IBinder? {
       return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "greenpass:lock").apply {
            acquire()
        }
        Log.i(TAG, "Location Service Created")
        val NOTIFICATION_CHANNEL_ID = "com.example.greenpass"
        val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notifManager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        notifManager.createNotificationChannel(chan)
        val notificationBuilder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        val notification: Notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Green Pass Tracker")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText("Tracking your location")
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()

        startForeground(1023456789, notification)
        requestLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)

//        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Issue with permissions")
            return
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.i(TAG,"Service task removed")
        Toast.makeText(this,"Service killed",Toast.LENGTH_LONG).show()
        stopForeground(true)
        notifManager.cancel(1023456789)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        Log.i(TAG,"Service Killed")
        Toast.makeText(this,"Service killed",Toast.LENGTH_LONG).show()
        stopForeground(true)
        notifManager.cancel(1023456789)
        stopSelf()
        wakeLock.release()
//        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
        super.onDestroy()
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 4 * 60 * 1000
        request.fastestInterval = 4 * 60 * 1000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request,object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
//                     For testing purposes
//                    val infraction = InfractionRecord("test", LocalDateTime.now())
//                    infraction.writeToDatabase()
//                    InfractionRecord.sendInfractionNotification(applicationContext,infraction)

                    super.onLocationResult(p0)
                    val loc = p0?.lastLocation
                    Log.i("ChildLocServices","Last Location ${loc.toString()}")
                    if(loc != null){
                        Database.writeLocation(loc)
                        Log.i("ChildLocServices","Location updated to firebase")
                        if(lastLocation!=null)
                            checkTresspass(lastLocation!!,loc)
                        lastLocation = loc
                    }

                }
            }, null)
        } else{
            Log.i(TAG,"Permission not granted for location update")
        }
    }

    fun isInGeofence(loc: Location, latLng: LatLng, radius:Double) : Boolean {
        return SphericalUtil.computeDistanceBetween(LatLng(loc.latitude,loc.longitude),latLng) <= radius
    }

    fun checkTresspass(oldLoc: Location, newLoc:Location){
        fun _checkTresspass(N: Int=-1){
            if(N==-1){
                Firebase.database.reference
                    .child("geofences")
                    .child("N").get().addOnSuccessListener {
                        _checkTresspass(it.value.toString().toInt())
                    }
            } else {
                for (i in 0 until N) {
                    Firebase.database.reference
                        .child("geofences")
                        .child(i.toString()).get().addOnSuccessListener {
                            val name = it.child("name").value.toString()
                            val lat = it.child("lat").value.toString().toDouble()
                            val long = it.child("long").value.toString().toDouble()
                            val radius = it.child("radius").value.toString().toDouble()
                            val clearance = it.child("clearance").value.toString().toInt()
                            val latLng = LatLng(lat,long)

                            if (Database.user!!.clearance <= Clearance.findByValue(clearance)!!
                                && !isInGeofence(oldLoc,latLng,radius)
                                && isInGeofence(newLoc,latLng,radius)){

                                val infraction = InfractionRecord(name, LocalDateTime.now())
                                infraction.writeToDatabase()
                                InfractionRecord.sendInfractionNotification(this.applicationContext,infraction)
                            }
                        }
                }
            }
        }
        _checkTresspass()
    }

    companion object{
        val TAG = this::class.simpleName
    }

}