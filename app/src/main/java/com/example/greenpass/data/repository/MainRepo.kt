package com.example.greenpass.data.repository

import com.example.greenpass.data.api.ApiHelper

class MainRepo(private val apiHelper: ApiHelper) {
    suspend fun getCountries() = apiHelper.getCountries()
}