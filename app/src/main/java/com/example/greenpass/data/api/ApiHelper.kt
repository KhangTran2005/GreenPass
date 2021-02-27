package com.example.greenpass.data.api

class ApiHelper(private val apiService: GetService) {
    suspend fun getCountries() = apiService.getCountries()
}