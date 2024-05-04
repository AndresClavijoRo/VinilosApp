package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IArtistEndpoint {

    @GET("musicians")
    fun getArtistList(): Call<ArrayList<ArtistDto>>;

    @GET("musicians/{musicianId}")
    fun getPerformerById(@Path("musicianId") musicianId: Int): Call<ArtistDto>;
}