package com.example.greenpass.ui.covidinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenpass.data.api.InfoApi
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.News
import kotlinx.coroutines.launch
import java.lang.Exception

class NewsViewModel : ViewModel() {
    private val _responseVaccine = MutableLiveData<News>()
    private val _responseHealth = MutableLiveData<News>()
    private val _responseCorona = MutableLiveData<News>()

    val responseVaccine: LiveData<News>
        get() = _responseVaccine
    val responseHealth: LiveData<News>
        get() = _responseHealth
    val responseCorona: LiveData<News>
        get() = _responseCorona

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try{
                val vaccine = InfoApi.retrofitService.getVaccNews()
                val health = InfoApi.retrofitService.getHealthNews()
                val corona = InfoApi.retrofitService.getCovNews()

                _responseVaccine.value = vaccine
                _responseHealth.value = health
                _responseCorona.value = corona
            }
            catch(e: Exception){

            }
        }
    }
}