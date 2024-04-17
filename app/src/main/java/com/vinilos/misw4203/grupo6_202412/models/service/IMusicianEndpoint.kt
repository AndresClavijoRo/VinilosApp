package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.MusicianDto
import retrofit2.Call
import retrofit2.http.GET

interface IMusicianEndpoint {

    @GET("/musicians")
    fun getMusicianList(): Call<ArrayList<MusicianDto>>;
}