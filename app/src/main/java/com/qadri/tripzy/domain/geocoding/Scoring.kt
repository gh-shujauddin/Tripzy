package com.qadri.tripzy.domain.geocoding


import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.geocoding.FieldScore
import kotlinx.serialization.Serializable

@Serializable
data class Scoring(
    @SerializedName("fieldScore")
    val fieldScore: FieldScore?,
    @SerializedName("queryScore")
    val queryScore: Double?
)