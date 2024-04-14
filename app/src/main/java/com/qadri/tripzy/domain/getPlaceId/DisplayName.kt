package com.qadri.tripzy.domain.getPlaceId


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DisplayName(
    @SerializedName("languageCode")
    val languageCode: String?,
    @SerializedName("text")
    val text: String?
)