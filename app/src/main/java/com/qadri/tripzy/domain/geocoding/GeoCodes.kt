package com.qadri.tripzy.domain.geocoding


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.geocoding.Item
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodes(
    @SerializedName("items")
    val items: List<Item?>?
)