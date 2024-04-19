package com.vinilos.misw4203.grupo6_202412.models.repository

import android.util.Log
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VinilosRepository(private val webService:VinilosService) {
    companion object Factory {
        fun create(webService:VinilosService): VinilosRepository {
            return VinilosRepository(webService)
        }
    }

    fun getPerformers(onResponse:(resp:ArrayList<ArtistDto>)->Unit, onFailure:(resp:String)->Unit){
        webService.getArtistEndpoint().getArtistList()
            .enqueue(object: Callback<ArrayList<ArtistDto>> {
                override fun onResponse(call: Call<ArrayList<ArtistDto>>, response: Response<ArrayList<ArtistDto>>) {
                    onResponse(response.body()!!)
                }

                override fun onFailure(call: Call<ArrayList<ArtistDto>>, t: Throwable) {
                    Log.e("API ERROR", t.message!!, t )
                    onFailure(t.message!!)
                }
            })
    }

    fun getCollectors(onResponse:(resp:ArrayList<CollectorDto>)->Unit, onFailure:(resp:String)->Unit){
        webService.getCollectorEndpoint().getCollectorsList()
            .enqueue(object: Callback<ArrayList<CollectorDto>> {
                override fun onResponse(call: Call<ArrayList<CollectorDto>>, response: Response<ArrayList<CollectorDto>>) {
                    onResponse(response.body()!!)
                }

                override fun onFailure(call: Call<ArrayList<CollectorDto>>, t: Throwable) {
                    Log.e("API ERROR", t.message!!, t )
                    onFailure(t.message!!)
                }
            })
    }
}