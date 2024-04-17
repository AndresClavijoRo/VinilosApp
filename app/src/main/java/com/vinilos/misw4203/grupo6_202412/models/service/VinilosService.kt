package com.vinilos.misw4203.grupo6_202412.models.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VinilosService(private val baseUrl: String) {
    companion object Factory {
        fun create(baseUrl: String): VinilosService {
            return VinilosService(baseUrl)
        }
    }

    private val api: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getMusicianEndpoint():IMusicianEndpoint {
        return api.create(IMusicianEndpoint::class.java)
    }

}