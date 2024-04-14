package com.qadri.tripzy.domain

import kotlinx.serialization.Serializable

@Serializable
data class GetPlacePhotos(
    val `data`: List<PhotoData>,
    val paging: Paging
)

@Serializable
data class PhotoData(
    val caption: String,
    val helpful_votes: String,
    val id: String,
    val images: ImagesClass,
    val locations: List<Location>,
    val published_date: String,
    val uploaded_date: String
)

@Serializable
data class Paging(
    val results: String,
    val total_results: String
)

@Serializable
data class ImagesClass(
    val original: Original,
)

@Serializable
data class Location(
    val id: String,
    val name: String
)

@Serializable
data class UserClass(
    val avatar: Avatar,
    val created_time: String,
    val first_name: String,
    val last_initial: String,
    val link: String,
    val locale: String,
    val member_id: String,
    val name: String,
    val points: String,
    val type: String,
    val user_id: String,
    val user_location: UserLocation,
    val username: String
)

@Serializable
data class LargeClass(
    val height: String,
    val url: String,
    val width: String
)

@Serializable
data class Medium(
    val height: String,
    val url: String,
    val width: String
)

@Serializable
data class Original(
    val height: String,
    val url: String,
    val width: String
)

@Serializable
data class Thumbnail(
    val height: String,
    val url: String,
    val width: String
)

@Serializable
data class Avatar(
    val large: LargeX,
    val small: SmallX
)

@Serializable
data class UserLocation(
    val id: String,
    val name: String
)

@Serializable
data class LargeX(
    val url: String
)

@Serializable
data class SmallX(
    val url: String
)