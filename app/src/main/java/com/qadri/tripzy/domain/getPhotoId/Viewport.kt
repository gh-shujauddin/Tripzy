package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.getPhotoId.Northeast
import com.qadri.tripzy.domain.getPhotoId.Southwest
import kotlinx.serialization.Serializable

@Serializable
data class Viewport(
    @SerializedName("northeast")
    val northeast: Northeast?,
    @SerializedName("southwest")
    val southwest: Southwest?
)