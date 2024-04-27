package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import retrofit2.Call
import retrofit2.http.GET

interface IArtistEndpoint {

    @GET("musicians")
    fun getArtistList(): Call<ArrayList<ArtistDto>>;
}