package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto (
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("cover") var cover: String? = null,
    @SerializedName("releaseDate") var releaseDate: String? = null,
    @SerializedName("genre") var genre: String? = null,
    @SerializedName("recordLabel") var recordLabel: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tracks") var tracks: ArrayList<TraksDto> = arrayListOf(),
    @SerializedName("performers") var performers: ArrayList<ArtistDto> = arrayListOf(),
    @SerializedName("comments") var comments: ArrayList<CommentDto> = arrayListOf()
)