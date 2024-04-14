package com.qadri.tripzy.domain.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lng")
    val lng: Double?
)