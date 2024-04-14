package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    @SerializedName("location")
    val location: Location?,
    @SerializedName("viewport")
    val viewport: Viewport?
)