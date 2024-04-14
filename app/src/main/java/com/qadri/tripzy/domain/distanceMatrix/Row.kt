package com.qadri.tripzy.domain.distanceMatrix


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Row(
    @SerializedName("elements")
    val elements: List<Element?>?
)