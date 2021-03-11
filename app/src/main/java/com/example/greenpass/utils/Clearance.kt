package com.example.greenpass.utils

enum class Clearance {
    ANY, OUT_PATIENT, OFFICER, ADMIN;

    companion object{
        fun getDesc(clearance: String): String{
            if (clearance.equals("OutPatient")){
                return "You have taken the Covid Vaccine and are " +
                        "allowed to enter and use public venues and services"
            }
            return ""
        }
    }
}