package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorAlbumDetailDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ICollectorEndpoint {
    @GET("collectors")
    fun getCollectorsList(): Call<ArrayList<CollectorDto>>;

    @GET("collectors/{id}")
    fun getCollectorById(@Path("id") id: Int): Call<CollectorDto>;

    @GET("collectors/{id}/albums")
    fun getCollectorAlbumsById(@Path("id") id: Int): Call<ArrayList<CollectorAlbumDetailDto>>;
}