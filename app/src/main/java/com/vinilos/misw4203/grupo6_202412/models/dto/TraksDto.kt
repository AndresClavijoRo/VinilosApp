package com.vinilos.misw4203.grupo6_202412.models.dto
import com.google.gson.annotations.SerializedName
class TraksDto (
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("duration") var duration: String,
)
