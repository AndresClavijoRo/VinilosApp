package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class CollectorDto (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("telephone") var telephone: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("comments") var comments: ArrayList<CommentDto> = arrayListOf(),
    @SerializedName("favoritePerformers") var favoritePerformers: ArrayList<ArtistDto> = arrayListOf(),
    @SerializedName("collectorAlbums") var collectorAlbums: ArrayList<CollectorAlbumDto> = arrayListOf()
)