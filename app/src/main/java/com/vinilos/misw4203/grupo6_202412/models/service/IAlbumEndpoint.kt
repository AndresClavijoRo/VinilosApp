package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IAlbumEndpoint {
    @GET("albums")
    fun getAlbumList(): Call<ArrayList<AlbumDto>>;

    @GET("albums/{id}")
    fun getAlbumById(@Path("id") id: Int): Call<AlbumDto>
}