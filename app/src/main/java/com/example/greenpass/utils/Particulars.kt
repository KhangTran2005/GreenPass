package com.example.greenpass.utils

import android.content.Context
import kotlinx.android.synthetic.main.fragment_log_in.*

object Particulars {
    fun writeUserName(username:String?, context: Context){
        val prefs = context.getSharedPreferences("particulars",Context.MODE_PRIVATE)
        with(prefs.edit()){
            putString("username",username)
            apply()
        }
    }
    fun getUsername(context: Context) : String? {
        val prefs = context.getSharedPreferences("particulars",Context.MODE_PRIVATE)
        return prefs.getString("username",null)
    }
}