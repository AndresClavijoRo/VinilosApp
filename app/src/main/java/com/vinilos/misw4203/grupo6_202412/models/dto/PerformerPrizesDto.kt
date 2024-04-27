package com.vinilos.misw4203.grupo6_202412.models.dto

import com.google.gson.annotations.SerializedName

data class PerformerPrizesDto (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("premiationDate") var premiationDate: String? = null
)