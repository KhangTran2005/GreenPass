package com.example.greenpass.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.greenpass.MainActivity
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class InfractionRecord(
    val locationName: String,
    val date: LocalDateTime
){
    constructor(locationName: String, date: String) :
            this(locationName, LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    fun getReadableDate(): String{
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date)
    }
    fun getISODate(): String{
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    fun writeToDatabase() {
        fun _writeToDatabase(N: Int = -1){
            if(N == -1) {
                Firebase.database.reference
                    .child("users")
                    .child(Database.username)
                    .child("infractions")
                    .child("N").get().addOnSuccessListener {
                        val n = it.value.toString().toInt()
                        it.ref.setValue(n+1)
                        _writeToDatabase(n)
                    }
            } else {
                Firebase.database.reference
                    .child("users")
                    .child(Database.username)
                    .child("infractions")
                    .child(N.toString()).get().addOnSuccessListener {
                        it.child("location").ref.setValue(locationName)
                        it.child("time").ref.setValue(getISODate())
                    }
            }
        }
        _writeToDatabase()

    }

    companion object {
        fun sendInfractionNotification(context: Context, infractionRecord: InfractionRecord){
            val builder = NotificationCompat.Builder(context, "101")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Geofence Trespass")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("You have entered ${infractionRecord.locationName} at ${infractionRecord.getReadableDate()}. This infraction will be recorded."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            createNotificationChannel(context,"You entered ${infractionRecord.locationName} at ${infractionRecord.getReadableDate()}")
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(101, builder.build())
            }
        }

        private fun createNotificationChannel(context: Context, descriptionText: String) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Geofence Trespass"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel("101", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

    }


}