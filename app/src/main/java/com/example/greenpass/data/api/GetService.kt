package com.example.greenpass.data.api

import com.example.greenpass.data.model.Country
import retrofit2.Call
import retrofit2.http.GET

interface GetService {
    @GET("countries")
    suspend fun getCountries() : List<Country>
}