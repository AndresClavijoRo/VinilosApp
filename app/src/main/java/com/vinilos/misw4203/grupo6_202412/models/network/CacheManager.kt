package com.vinilos.misw4203.grupo6_202412.models.network

import android.util.SparseArray
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto

class CacheManager private constructor() {
    companion object{
        private var instance: CacheManager? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CacheManager().also {
                    instance = it
                }
            }
    }

    private var albumsList: SparseArray<AlbumDto> = SparseArray()
    fun addAlbum(albumId: Int, album: AlbumDto) {
        if (albumsList[albumId] == null) {
            albumsList.append(albumId, album)
        }
    }

    fun getAlbum(albumId: Int): AlbumDto? {
        return when {
            (albumsList[albumId] != null) -> albumsList[albumId]!!
            else -> null
        }
    }
}