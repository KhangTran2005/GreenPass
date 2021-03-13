package com.example.greenpass.data.model

import com.example.greenpass.utils.Clearance
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String,
    var ID: String,
    var age: String,
    var DoB: String,
    var nationality: String,
    var sex: String,
    var clearance: Clearance?,
    var vacc_date: String?,
    var vacc_loc: String?
) {
}