package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import retrofit2.Call
import retrofit2.http.GET

interface ICollectorEndpoint {
    @GET("collectors")
    fun getCollectorsList(): Call<ArrayList<CollectorDto>>;
}