package com.example.greenpass.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.greenpass.data.api.ApiHelper
import com.example.greenpass.data.repository.MainRepo
import com.example.greenpass.ui.covidinfo.CovidInfoViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CovidInfoViewModel::class.java)) {
            return CovidInfoViewModel(MainRepo(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}