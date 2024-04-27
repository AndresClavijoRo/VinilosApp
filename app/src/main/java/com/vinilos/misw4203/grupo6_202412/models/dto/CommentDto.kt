package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class CommentDto (
    @SerializedName("id") var id          : Int?    = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("rating") var rating      : Int?    = null
)
