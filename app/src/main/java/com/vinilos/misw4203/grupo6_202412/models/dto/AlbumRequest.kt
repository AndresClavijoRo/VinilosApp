package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class AlbumRequest (
    @SerializedName("name") var name: String,
    @SerializedName("cover") var cover: String,
    @SerializedName("releaseDate") var releaseDate: String,
    @SerializedName("genre") var genre: String,
    @SerializedName("recordLabel") var recordLabel: String,
    @SerializedName("description") var description: String
)