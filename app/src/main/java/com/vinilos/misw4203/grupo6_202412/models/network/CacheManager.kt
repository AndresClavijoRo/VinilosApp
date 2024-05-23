package com.vinilos.misw4203.grupo6_202412.models.network

import android.util.SparseArray
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto

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

    private var listAlbumDto: SparseArray<List<AlbumDto>> = SparseArray()
    fun addAlbums(albums: List<AlbumDto>) {
        listAlbumDto.put(1, albums)
    }

    fun getAlbums(): List<AlbumDto> {
        return when {
            (listAlbumDto[1] != null) -> listAlbumDto[1]
            else -> emptyList()
        }
    }

    fun clearAlbumsCache() {
        listAlbumDto.clear()
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

    private var listPerformerDto: SparseArray<List<ArtistDto>> = SparseArray()
    fun addPerformers(performers: List<ArtistDto>) {
        listPerformerDto.put(1, performers)
    }
    fun getPerformers(): List<ArtistDto> {
        return when {
            (listPerformerDto[1] != null) -> listPerformerDto[1]
            else -> emptyList()
        }
    }

    private var listCollectorDto: SparseArray<List<CollectorDto>> = SparseArray()
    fun addCollectors(collectors: List<CollectorDto>) {
        listCollectorDto.put(1, collectors)
    }
    fun getCollectors(): List<CollectorDto> {
        return when {
            (listCollectorDto[1] != null) -> listCollectorDto[1]
            else -> emptyList()
        }
    }
}