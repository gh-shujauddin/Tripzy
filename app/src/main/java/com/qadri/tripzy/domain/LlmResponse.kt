package com.qadri.tripzy.domain

import com.google.gson.annotations.SerializedName
import com.qadri.tripzy.domain.Candidate
import kotlinx.serialization.Serializable

@Serializable
data class PalmApi(
    @SerializedName("candidates")
    val candidates: List<Candidate?>?
)