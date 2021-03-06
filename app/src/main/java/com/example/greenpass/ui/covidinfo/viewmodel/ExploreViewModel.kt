package com.example.greenpass.ui.covidinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenpass.data.api.InfoApi
import com.example.greenpass.data.model.Country
import kotlinx.coroutines.launch
import java.lang.Exception

class ExploreViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Country>>()
    private val _responseFake = MutableLiveData<String>()

    val response: LiveData<List<Country>>
        get() = _response
    val reponseFake: LiveData<String>
        get() = _responseFake

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try{
                val countries = InfoApi.retrofitService.getCountries()
                _responseFake.value = "Server have retrieved ${countries.size}"
                _response.value = countries
            }
            catch(e: Exception){
                _responseFake.value = "The thing broke down: ${e.message}"
            }
        }
    }
}