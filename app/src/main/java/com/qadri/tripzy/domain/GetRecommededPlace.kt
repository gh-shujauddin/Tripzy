package com.qadri.tripzy.domain
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class GetRecommendedPlace(
    @SerialName("data")
    val `data`: List<DataClass>?
)

@Serializable
data class DataClass(
    val location_id: String,
    @SerialName("address")
    val address: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("photo")
    val photo: PhotoRecClass?,

)

@Serializable
data class PhotoRecClass (
    val images: ImagesClass
)


@Serializable
data class Subcategory(
    val name: String?
)

@Serializable
data class ExcludeLocations(
    @SerialName("all")
    val all: All?,
    @SerialName("filtered")
    val filtered: Filtered?
)


@Serializable
data class All(
    @SerialName("count")
    val count: String?,
    @SerialName("label")
    val label: String?
)

@Serializable
data class Filtered(
    @SerialName("count")
    val count: String?,
    @SerialName("label")
    val label: String?
)