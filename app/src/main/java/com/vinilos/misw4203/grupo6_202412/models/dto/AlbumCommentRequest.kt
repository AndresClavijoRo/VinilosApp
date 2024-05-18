package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class AlbumCommentRequest(
    @SerializedName("description") var description: String,
    @SerializedName("rating") var rating: Int,
    @SerializedName("collector") var collector: CollectorDto
)
