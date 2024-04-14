package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.getPhotoId.Close
import com.qadri.tripzy.domain.getPhotoId.Open
import kotlinx.serialization.Serializable

@Serializable
data class Period(
    @SerializedName("close")
    val close: Close?,
    @SerializedName("open")
    val `open`: Open?
)