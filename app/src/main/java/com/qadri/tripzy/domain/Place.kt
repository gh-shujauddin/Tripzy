package com.qadri.tripzy.domain

data class Place(
    val name: String?,
    val link: String?,
    val locationName: String?,
    val description: String?,
    val rating: Double?,
    val totalReviews: Int? = 0,
    val budgetPerDay: Int? = 0,
    val tags: Set<String?> = setOf(),
    val averageTemperature: Double? = 0.0,
    val distance: Double? = 0.0,
    val phone: String? = null,
    val email: String? = null,
    val bookingLink: String? = null,
    val recommendedTripTime: String? = null,
    val ranking: String? = null,
    val nearestMetro: List<NearestMetro>? = null,
    val isClosed: Boolean? = true,
    val reviews: List<Reviews>? = null,
    val geoPoint: GeoCode? = null
)

data class NearestMetro(
    val name: String?,
    val address: String?
)

data class Reviews(
    val title: String? = null,
    val rating: String? = null,
    val publishedDate: String? = null,
    val summary: String? = null,
    val author: String? = null,
    val url: String? = null
)