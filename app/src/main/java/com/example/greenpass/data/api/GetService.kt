package com.example.greenpass.data.api

import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.Vaccine
import retrofit2.Call
import retrofit2.http.GET

interface GetService {
    @GET("countries")
    suspend fun getCountries() : List<Country>

    @GET("vaccine")
    fun getVaccines() : Vaccine
}