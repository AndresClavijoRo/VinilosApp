package com.vinilos.misw4203.grupo6_202412.models.service

import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumCommentRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorAlbumDetailDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class VinilosService(private val baseUrl: String) {

    companion object Factory {
        private var INSTANCE: VinilosService? = null
        fun getInstance(baseUrl: String): VinilosService =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VinilosService(baseUrl).also { INSTANCE = it }
            }
    }

    private val api: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val getAlbumEndpoint: IAlbumEndpoint by lazy {
        api.create(IAlbumEndpoint::class.java)
    }

    private val getArtistEndpoint: IArtistEndpoint by lazy {
        api.create(IArtistEndpoint::class.java)
    }

    private val getCollectorEndpoint: ICollectorEndpoint by lazy {
        api.create(ICollectorEndpoint::class.java)
    }

    fun getAlbums(
        onResponse: (resp: ArrayList<AlbumDto>) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getAlbumEndpoint.getAlbumList()
            .enqueue(responseCallback<ArrayList<AlbumDto>>(onResponse, onFailure))
    }

    fun createAlbums(
        request: AlbumRequest,
        onResponse: (resp: AlbumDto) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getAlbumEndpoint.createAlbum(request)
            .enqueue(responseCallback<AlbumDto>(onResponse, onFailure))
    }
    fun createAlbumComment(
        albumId: Int,
        request: AlbumCommentRequest,
        onResponse: (resp: CommentDto) -> Unit,
        onFailure: (resp: String) -> Unit
    ){
        getAlbumEndpoint.createAlbumComment(albumId, request)
            .enqueue(responseCallback<CommentDto>(onResponse, onFailure))
    }

    fun getAlbumById(
        id: Int,
        onResponse: (resp: AlbumDto) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getAlbumEndpoint.getAlbumById(id)
            .enqueue(responseCallback<AlbumDto>(onResponse, onFailure))
    }

    fun getPerformers(
        onResponse: (resp: ArrayList<ArtistDto>) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getArtistEndpoint.getArtistList()
            .enqueue(responseCallback<ArrayList<ArtistDto>>(onResponse, onFailure))
    }

    fun getPerformerById(
        onResponse: (resp: ArtistDto) -> Unit,
        onFailure: (resp: String) -> Unit,
        musicianId: Int
    ) {
        getArtistEndpoint.getPerformerById(musicianId)
            .enqueue(responseCallback<ArtistDto>(onResponse, onFailure))
    }

    fun getCollectors(
        onResponse: (resp: ArrayList<CollectorDto>) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getCollectorEndpoint.getCollectorsList()
            .enqueue(responseCallback<ArrayList<CollectorDto>>(onResponse, onFailure))
    }

    fun getCollectorById(
        id: Int,
        onResponse: (resp: CollectorDto) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
        getCollectorEndpoint.getCollectorById(id)
            .enqueue(responseCallback<CollectorDto>(onResponse, onFailure))
    }

    fun getCollectorAlbums(
        id: Int,
        onResponse: (resp: ArrayList<CollectorAlbumDetailDto>) -> Unit,
        onFailure: (resp: String) -> Unit
    ) {
       getCollectorEndpoint.getCollectorAlbumsById(id)
            .enqueue(
                responseCallback(
                    onResponse,
                    onFailure,
                )
            )
    }


    private fun <T> responseCallback(
        onResponse: (resp: T) -> Unit,
        onFailure: (resp: String) -> Unit
    ) = object : Callback<T> {
        override fun onResponse(p0: Call<T>, p1: Response<T>) {
            if (p1.isSuccessful) {
                p1.body()?.let { onResponse(it) }
            } else {
                onFailure("Response unsuccessful")
            }
        }

        override fun onFailure(p0: Call<T>, p1: Throwable) {
            onFailure(p1.message ?: "Unknown error")
        }
    }
}