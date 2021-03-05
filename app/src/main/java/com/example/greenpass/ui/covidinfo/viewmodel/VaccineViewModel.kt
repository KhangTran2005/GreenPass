package com.example.greenpass.ui.covidinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenpass.data.api.InfoApi
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.Vaccine
import kotlinx.coroutines.launch
import java.lang.Exception

class VaccineViewModel : ViewModel(){

    private val _response = MutableLiveData<Vaccine>()

    val response: LiveData<Vaccine>
        get() = _response

    init {
        fetchVaccine()
    }

    private fun fetchVaccine() {
        viewModelScope.launch {
            try{
                val vaccine = InfoApi.retrofitService.getVaccines()
                _response.value = vaccine
            }
            catch(e: Exception){

            }
        }
    }
}