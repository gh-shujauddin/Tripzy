package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OpenX(
    @SerializedName("day")
    val day: Int?,
    @SerializedName("time")
    val time: String?
)