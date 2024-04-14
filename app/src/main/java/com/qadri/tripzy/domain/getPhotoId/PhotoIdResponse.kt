package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoIdResponse(
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: String?
)