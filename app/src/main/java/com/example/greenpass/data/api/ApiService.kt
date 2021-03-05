package com.example.greenpass.data.api

import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.Vaccine
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private const val BASE_URL = "https://disease.sh/v3/covid-19/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface InfoApiService{
    @GET("countries")
    suspend fun getCountries() : List<Country>

    @GET("vaccine")
    suspend fun getVaccines() : Vaccine
}

object InfoApi {
    val retrofitService: InfoApiService by lazy {
        retrofit.create(InfoApiService::class.java)
    }
}

