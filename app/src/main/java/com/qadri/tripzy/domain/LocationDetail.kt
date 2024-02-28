package com.qadri.tripzy.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResult(
    val `data`: Data
)
@Serializable
data class Data(
    val Typeahead_autocomplete: TypeaheadAutocomplete
)

@Serializable
data class TypeaheadAutocomplete(
    val results: List<Result>
)

@Serializable
data class Result(
    @SerialName("detailsV2")
    val detailsV2: DetailsV2? ,
    @SerialName("image")
    val image: Image?
)

@Serializable
data class DetailsV2(
    val locationId: Int,
    val names: Names,
    val placeType: String,
    val geocode: GeoCode? = null
)

@Serializable
data class GeoCode(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class Image(
    val photo: Photo
)

@Serializable
data class Names(
    val longOnlyHierarchyTypeaheadV2: String,
    val name: String
)

@Serializable
data class Photo(
    val photoSizes: List<PhotoSize>
)

@Serializable
data class PhotoSize(
    val height: Int,
    val url: String,
    val width: Int
)