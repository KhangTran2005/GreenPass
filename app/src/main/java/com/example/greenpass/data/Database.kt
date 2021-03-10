package com.example.greenpass.data

import android.location.Location
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt
import java.io.File
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
        // Notice that passwords are indeed hashed with BCrypt
        fun createAccount(username: String, password: String) : String{
            Firebase.database.reference
                .child("usernamesToPassword")
                .child(username)
                .setValue("password",BCrypt.hashpw(password,BCrypt.gensalt()))

            val userId: String = generateUserId()
            Firebase.database.reference
                .child("usernamesToUserId")
                .child(username)
                .setValue("userId", userId)
            return userId
        }

        fun generateUserId() : String{
            val charPool = (65..65+25).map{ it.toChar() } + (97..97+25).map{ it.toChar() }
            return Date().time.toString() + (1..8)
                .map{ Random.nextInt(0,charPool.size) }
                .map{charPool::get}
                .joinToString("")
        }

        // username must be 8-20 characters long, with
        fun checkUsername(username: String): Boolean{
            return username.matches(Regex("^(?=.{8,20}\$)(?![_.])(?!.*[_.]{2})[\\w]+(?<![_.])\$"))
        }

        fun checkPassword(password: String): Boolean{
            return password.matches(Regex("^(?=\\S+).{6,}"))
        }

        fun loginAccount(username: String, password: String) : Boolean {
            var res: Boolean = false
            Firebase.database.reference
                .child("usernameToPassword")
                .child(username)
                .get().addOnSuccessListener {
                    if (BCrypt.checkpw(password,it.value as String))
                        res = true
                }
            return res
        }
    }
}