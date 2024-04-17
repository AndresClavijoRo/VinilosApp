package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class CollectorAlbumDto (
    @SerializedName("id") var id     : Int?    = null,
    @SerializedName("price") var price  : Int?    = null,
    @SerializedName("status") var status : String? = null
)