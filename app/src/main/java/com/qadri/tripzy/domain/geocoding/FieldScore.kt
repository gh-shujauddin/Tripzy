package com.qadri.tripzy.domain.geocoding


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FieldScore(
    @SerializedName("placeName")
    val placeName: Double?,
    @SerializedName("state")
    val state: Double?
)