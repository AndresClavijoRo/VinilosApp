package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class FavoritePerformerDto (
    @SerializedName("id") var id          : Int?    = null,
    @SerializedName("name") var name        : String? = null,
    @SerializedName("image") var image       : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("birthDate") var birthDate   : String? = null
)