package com.qadri.tripzy.domain.hereSearch


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Highlights(
    @SerializedName("address")
    val address: AddressX?,
    @SerializedName("title")
    val title: List<Title>?
)