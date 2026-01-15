package com.example.museumapp.network

import com.example.museumapp.model.EuropeanaResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EuropeanaApi {
    @GET("record/v2/search.json")
    suspend fun searchArtworks(
        @Query("wskey") apiKey: String,
        @Query("query") query: String,
        @Query("rows") rows: Int = 50
    ): EuropeanaResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.europeana.eu/"

    val api: EuropeanaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EuropeanaApi::class.java)
    }
}