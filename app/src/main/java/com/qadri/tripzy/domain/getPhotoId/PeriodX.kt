package com.qadri.tripzy.domain.getPhotoId


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.getPhotoId.CloseX
import com.qadri.tripzy.domain.getPhotoId.OpenX
import kotlinx.serialization.Serializable

@Serializable
data class PeriodX(
    @SerializedName("close")
    val close: CloseX?,
    @SerializedName("open")
    val `open`: OpenX?
)