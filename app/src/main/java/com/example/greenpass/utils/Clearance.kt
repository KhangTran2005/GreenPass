package com.example.greenpass.utils

enum class Clearance(val value:Int) {
    ANY(0), OUT_PATIENT(1), OFFICER(2), ADMIN(3);

    val labels = listOf("Any","OutPatient","Officer","ADMIN")

    override fun toString(): String {
        return labels[values().indexOf(this)]
    }

    companion object{
        fun getDesc(clearance: String): String{
            if (clearance.equals("OutPatient")){
                return "You have taken the Covid Vaccine and are " +
                        "allowed to enter and use public venues and services"
            }
            return ""
        }
        private val types = values().associateBy { it.value }
        fun findByValue(value: Int) = types[value]
    }
}