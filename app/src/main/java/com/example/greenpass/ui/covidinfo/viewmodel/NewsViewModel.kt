package com.example.greenpass.ui.covidinfo.viewmodel

import android.util.Log
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

    private val _response = MutableLiveData<List<News.Article>>()

    val response: LiveData<List<News.Article>>
        get() = _response

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try{
                val vaccine = InfoApi.retrofitService.getVaccNews()
                val health = InfoApi.retrofitService.getHealthNews()
                val corona = InfoApi.retrofitService.getCovNews()

                val vaccineArticles = vaccine.news
                val healthArticles = health.news
                val coronaArticles = corona.news

                Log.d("debug", vaccineArticles.toString())

                _response.value = listOf(vaccineArticles, healthArticles, coronaArticles).flatten()
            }
            catch(e: Exception){

            }
        }
    }
}