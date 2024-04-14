package com.qadri.tripzy.domain.getPlaceId


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.getPlaceId.Place
import kotlinx.serialization.Serializable

@Serializable
data class PlaceIdResponse(
    @SerializedName("places")
    val places: List<Place?>?
)