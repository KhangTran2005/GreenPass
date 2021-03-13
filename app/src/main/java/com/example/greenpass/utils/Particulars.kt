package com.example.greenpass.utils

import android.content.Context
import com.example.greenpass.data.model.User
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Particulars {
    fun writeUserName(username:String?, context: Context){
        val prefs = context.getSharedPreferences("particulars",Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString("username",username)
            apply()
        }
    }
    fun writeUserInfo(user: User?, context: Context){
        val prefs = context.getSharedPreferences("particulars", Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString("user", Json.encodeToString(user))
            apply()
        }
    }
    fun getUser(context: Context) : User? {
        val prefs = context.getSharedPreferences("particulars", Context.MODE_PRIVATE)
        val userJson: String? = prefs.getString("user", null)
        return if (userJson == null){
            null
        } else {
            Json.decodeFromString(userJson)
        }
    }
    fun getUsername(context: Context) : String? {
        val prefs = context.getSharedPreferences("particulars",Context.MODE_PRIVATE)
        return prefs.getString("username",null)
    }
}