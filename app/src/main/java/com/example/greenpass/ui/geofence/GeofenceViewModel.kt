package com.example.greenpass.ui.geofence

import android.view.Gravity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.Geofence
import com.mancj.slideup.SlideUp
import com.mancj.slideup.SlideUpBuilder

class GeofenceViewModel : ViewModel() {

    val geofences: MutableLiveData<List<Geofence>> = MutableLiveData(listOf())
    val isDialogOpen = MutableLiveData(false)


    fun fetchGeofences() {
        
    }

    fun adjustDialog(slideUp: SlideUp){
        if(isDialogOpen.value == null)
            return
        if (isDialogOpen.value!!){
            slideUp.hide()
        } else {
            slideUp.show()
        }
        isDialogOpen.value = isDialogOpen.value?.not()
    }

}