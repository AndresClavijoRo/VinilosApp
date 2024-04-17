package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class MusicianDto (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("birthDate") var birthDate: String? = null,
    @SerializedName("albums") var albums: ArrayList<AlbumsDto> = arrayListOf(),
    @SerializedName("performerPrizes") var performerPrizes: ArrayList<PerformerPrizesDto> = arrayListOf()
)
