package com.qadri.tripzy.domain

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    @SerializedName("output")
    val output: String?,
    @SerializedName("safetyRatings")
    val safetyRatings: List<SafetyRating?>?
)