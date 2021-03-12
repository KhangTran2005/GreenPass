package com.example.greenpass.ui.infractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenpass.data.Database
import com.example.greenpass.utils.InfractionRecord
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InfractionViewModel : ViewModel() {

    private val _response = MutableLiveData<MutableList<InfractionRecord>>()

    val response: LiveData<MutableList<InfractionRecord>>
        get() = _response

    init {
        fetchInfractions()
    }

    private fun fetchInfractions() {
        Firebase.database.reference
            .child("users")
            .child(Database.username)
            .child("infractions")
            .child("N").get().addOnSuccessListener {
                val N = it.value.toString().toInt()
                for(i in 0 until N-1){
                    Firebase.database.reference
                        .child("users")
                        .child(Database.username)
                        .child("infractions")
                        .child(i.toString()).get().addOnSuccessListener {
                            response.value?.add(InfractionRecord(
                                it.child("location").value.toString(),
                                it.child("date").value.toString()
                            ))
                        }
                }
            }
    }
}