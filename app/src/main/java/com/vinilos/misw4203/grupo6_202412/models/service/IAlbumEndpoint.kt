package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import retrofit2.Call
import retrofit2.http.GET

interface IAlbumEndpoint {
    @GET("albums")
    fun getAlbumList(): Call<ArrayList<AlbumDto>>;
}