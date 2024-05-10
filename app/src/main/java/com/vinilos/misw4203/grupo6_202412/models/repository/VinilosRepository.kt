package com.vinilos.misw4203.grupo6_202412.models.repository


import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

open class VinilosRepository(private val webService:VinilosService) {
    companion object Factory {
        fun create(webService:VinilosService): VinilosRepository {
            return VinilosRepository(webService)
        }
    }

    fun getAlbums(onResponse:(resp:ArrayList<AlbumDto>)->Unit, onFailure:(resp:String)->Unit){
        webService.getAlbums(onResponse, onFailure)
    }

    fun getAlbumById(id:Int, onResponse:(resp:AlbumDto)->Unit, onFailure:(resp:String)->Unit){
        webService.getAlbumById(id, onResponse, onFailure)
    }

    suspend fun createAlbums(request: AlbumRequest): AlbumDto = suspendCoroutine { cont ->
        webService.createAlbums(request,
            { cont.resume(it) },
            { cont.resumeWithException(Exception(it)) })
    }

    fun getPerformers(onResponse:(resp:ArrayList<ArtistDto>)->Unit, onFailure:(resp:String)->Unit){
        webService.getPerformers(onResponse, onFailure)
    }

    fun getCollectors(onResponse:(resp:ArrayList<CollectorDto>)->Unit, onFailure:(resp:String)->Unit){
        webService.getCollectors(onResponse, onFailure)
    }



}