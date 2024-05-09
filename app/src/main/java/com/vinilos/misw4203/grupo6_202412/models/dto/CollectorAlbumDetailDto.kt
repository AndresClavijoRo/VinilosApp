package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class CollectorAlbumDetailDto(
    @SerializedName("id") var id: Int,
    @SerializedName("price") var price: Int,
    @SerializedName("status") var status: String,
    @SerializedName("album") var album: AlbumDto,
    @SerializedName("collector") var collector: CollectorDto
)
