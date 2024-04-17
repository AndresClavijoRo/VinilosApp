package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("cover") var cover: String? = null,
    @SerializedName("releaseDate") var releaseDate: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("genre") var genre: String? = null,
    @SerializedName("recordLabel") var recordLabel: String? = null
)