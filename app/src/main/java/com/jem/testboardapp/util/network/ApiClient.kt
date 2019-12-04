package com.jem.testboardapp.util.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var BASE_URL = "http://15.164.93.58:8080"
    private var retrofit : Retrofit? = null

    fun getRetrofit() : Retrofit {
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit!!
    }
}