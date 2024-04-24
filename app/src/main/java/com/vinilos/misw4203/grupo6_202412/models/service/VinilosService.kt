package com.vinilos.misw4203.grupo6_202412.models.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class VinilosService(private val baseUrl: String) {
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

    fun getAlbumEndpoint(): IAlbumEndpoint {
        return api.create(IAlbumEndpoint::class.java)
    }

    fun getArtistEndpoint():IArtistEndpoint {
        return api.create(IArtistEndpoint::class.java)
    }

    fun getCollectorEndpoint(): ICollectorEndpoint {
        return api.create(ICollectorEndpoint::class.java)
    }

}