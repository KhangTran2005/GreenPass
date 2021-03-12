package com.example.greenpass.ui.covidinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenpass.data.api.InfoApi
import com.example.greenpass.data.model.ArticleWrapper
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.News
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class NewsViewModel : ViewModel() {

    private val _response = MutableLiveData<List<ArticleWrapper>>()

    val response: LiveData<List<ArticleWrapper>>
        get() = _response

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try{
                val vaccine = InfoApi.newsService.getVaccNews()
                val health = InfoApi.newsService.getHealthNews()
                val corona = InfoApi.newsService.getCovNews()

                val vaccineArticles = ArticleWrapper.toWrapper(vaccine.news, 1)
                val healthArticles = ArticleWrapper.toWrapper(health.news, 2)
                val coronaArticles = ArticleWrapper.toWrapper(corona.news, 3)

                val responseList: MutableList<ArticleWrapper> = mutableListOf(vaccineArticles, healthArticles, coronaArticles).flatten().toMutableList()

                responseList.shuffle()

                _response.value = responseList
            }
            catch(e: Exception){

            }
        }
    }
}