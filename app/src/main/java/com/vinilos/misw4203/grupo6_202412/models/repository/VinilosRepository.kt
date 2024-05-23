package com.vinilos.misw4203.grupo6_202412.models.repository


import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumCommentRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorAlbumDetailDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import com.vinilos.misw4203.grupo6_202412.models.network.CacheManager
import com.vinilos.misw4203.grupo6_202412.models.network.VinilosServiceAdapter
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

open class VinilosRepository(
    private val webService: VinilosServiceAdapter,
    private val cache: CacheManager
) {
    companion object Factory {
        fun create(webService: VinilosServiceAdapter, cache: CacheManager): VinilosRepository {
            return VinilosRepository(webService, cache)
        }
    }

    suspend fun getAlbums(): List<AlbumDto> = suspendCancellableCoroutine { cont ->
        cache.getAlbums().takeIf { it.isNotEmpty() }?.let {
            cont.resume(it)
            return@suspendCancellableCoroutine
        }

        webService.getAlbums({
            cache.addAlbums(it)
            cont.resume(it)
        }, { cont.resumeWithException(Exception(it)) })
    }

    suspend fun getAlbumById(id: Int): AlbumDto = suspendCancellableCoroutine { cont ->
        cache.getAlbum(id)?.let {
            cont.resume(it)
            return@suspendCancellableCoroutine
        }

        webService.getAlbumById(id, {
            cache.addAlbum(id, it)
            cont.resume(it)
        }, { cont.resumeWithException(Exception(it)) })
    }

    suspend fun createAlbums(request: AlbumRequest): AlbumDto = suspendCoroutine { cont ->
        cache.clearAlbumsCache()
        webService.createAlbums(request,
            { cont.resume(it) },
            { cont.resumeWithException(Exception(it)) })
    }

    suspend fun getPerformers(): List<ArtistDto> = suspendCancellableCoroutine { cont ->
        cache.getPerformers().takeIf { it.isNotEmpty() }?.let {
            cont.resume(it)
            return@suspendCancellableCoroutine
        }

        webService.getPerformers({
            cache.addPerformers(it)
            cont.resume(it)
        }, { cont.resumeWithException(Exception(it)) })
    }

    fun getPerformerById(onResponse:(resp:ArtistDto)->Unit, onFailure:(resp:String)->Unit, musicianId: Int){
        webService.getPerformerById(onResponse, onFailure, musicianId)
    }

    suspend fun getCollectors(): List<CollectorDto> = suspendCancellableCoroutine { cont ->
        cache.getCollectors().takeIf { it.isNotEmpty() }?.let {
            cont.resume(it)
            return@suspendCancellableCoroutine
        }

        webService.getCollectors({
            cache.addCollectors(it)
            cont.resume(it)
        }, { cont.resumeWithException(Exception(it)) })
    }

    fun getCollectorbyId(id:Int, onResponse:(resp:CollectorDto)->Unit, onFailure:(resp:String)->Unit) {
        return webService.getCollectorById(id, onResponse, onFailure)
    }

    fun getCollectorAlbums(id:Int, onResponse:(resp:ArrayList<CollectorAlbumDetailDto>)->Unit, onFailure:(resp:String)->Unit) {
        return webService.getCollectorAlbums(id, onResponse, onFailure)
    }

    suspend fun createAlbumComment(albumId: Int, request: AlbumCommentRequest): CommentDto = suspendCoroutine { cont ->
        webService.createAlbumComment(albumId,request,
            { cont.resume(it) },
            { cont.resumeWithException(Exception(it)) })
    }
}