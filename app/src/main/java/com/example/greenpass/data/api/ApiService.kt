package com.example.greenpass.data.api

import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.News
import com.example.greenpass.data.model.Vaccine
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private const val BASE_URL = "https://disease.sh/v3/covid-19/"
private const val BASE_URL_NEWS = "https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/news/"

private val okHttp = OkHttpClient.Builder().addInterceptor{ chain ->
    val request: Request = chain.request().newBuilder()
            .addHeader("x-rapidapi-key", "39b64fae87msh989732b2e41495cp1ce1edjsn4d625f39575e")
            .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
            .build()

    return@addInterceptor chain.proceed(request)
}

private val retrofit_news = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL_NEWS)
        .client(okHttp.build())
        .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface InfoApiService{

    @GET("countries")
    suspend fun getCountries() : List<Country>

    @GET("vaccine")
    suspend fun getVaccines() : Vaccine

    @GET("get-vaccine-news/0")
    suspend fun getVaccNews() : News

    @GET("get-health-news/1")
    suspend fun getHealthNews() : News

    @GET("get-coronavirus-news/0")
    suspend fun getCovNews() : News
}

object InfoApi {
    val retrofitService: InfoApiService by lazy {
        retrofit.create(InfoApiService::class.java)
    }

    val newsService: InfoApiService by lazy{
        retrofit_news.create(InfoApiService::class.java)
    }
}

