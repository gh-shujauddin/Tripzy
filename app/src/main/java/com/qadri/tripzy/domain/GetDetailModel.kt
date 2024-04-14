package com.qadri.tripzy.domain
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class GetDetailModel(
    @SerialName("address")
    val address: String?,
    @SerialName("address_obj")
    val addressObj: AddressObj?,
    @SerialName("ancestors")
    val ancestors: List<Ancestor>?,
    @SerialName("awards")
    val awards: List<String>?,
    @SerialName("bearing")
    val bearing: String?,
    @SerialName("booking")
    val booking: Booking?,
    @SerialName("category")
    val category: Category?,
    @SerialName("description")
    val description: String?,
    @SerialName("distance")
    val distance: String?,
    @SerialName("distance_string")
    val distanceString: String?,
    @SerialName("doubleclick_zone")
    val doubleclickZone: String?,
    @SerialName("fee")
    val fee: String?,
    @SerialName("has_panoramic_photos")
    val hasPanoramicPhotos: Boolean?,
    @SerialName("has_review_draft")
    val hasReviewDraft: Boolean?,
    @SerialName("hours")
    val hours: Hours?,
    @SerialName("is_candidate_for_contact_info_suppression")
    val isCandidateForContactInfoSuppression: Boolean?,
    @SerialName("is_closed")
    val isClosed: Boolean?,
    @SerialName("is_jfy_enabled")
    val isJfyEnabled: Boolean?,
    @SerialName("is_long_closed")
    val isLongClosed: Boolean?,
    @SerialName("is_product_review_eligible")
    val isProductReviewEligible: Boolean?,
    @SerialName("latitude")
    val latitude: String?,
    @SerialName("local_address")
    val localAddress: String?,
    @SerialName("local_lang_code")
    val localLangCode: String?,
    @SerialName("local_name")
    val localName: String?,
    @SerialName("location_id")
    val locationId: String?,
    @SerialName("location_string")
    val locationString: String?,
    @SerialName("location_subtype")
    val locationSubtype: String?,
    val email: String?,
    @SerialName("longitude")
    val longitude: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("nearest_metro_station")
    val nearestMetroStation: List<NearestMetroStation>?,
    @SerialName("neighborhood_info")
    val neighborhoodInfo: List<NeighborhoodInfo>?,
    @SerialName("num_reviews")
    val numReviews: String?,
    @SerialName("offer_group")
    val offerGroup: OfferGroup?,
    @SerialName("open_now_text")
    val openNowText: String?,
    @SerialName("parent_display_name")
    val parentDisplayName: String?,
    @SerialName("phone")
    val phone: String?,
    @SerialName("photo")
    val photo: PhotoClass?,
    @SerialName("photo_count")
    val photoCount: String?,
    @SerialName("preferred_map_engine")
    val preferredMapEngine: String?,
    @SerialName("ranking")
    val ranking: String?,
    @SerialName("ranking_category")
    val rankingCategory: String?,
    @SerialName("ranking_denominator")
    val rankingDenominator: String?,
    @SerialName("ranking_geo")
    val rankingGeo: String?,
    @SerialName("ranking_geo_id")
    val rankingGeoId: String?,
    @SerialName("ranking_position")
    val rankingPosition: String?,
    @SerialName("ranking_subcategory")
    val rankingSubcategory: String?,
    @SerialName("rating")
    val rating: String?,
    @SerialName("rating_histogram")
    val ratingHistogram: RatingHistogram?,
    @SerialName("raw_ranking")
    val rawRanking: String?,
    @SerialName("recommended_visit_length")
    val recommendedVisitLength: String?,
    @SerialName("reviews")
    val reviews: List<Review>?,
    @SerialName("subcategory")
    val subcategory: List<SubcategoryX>?,
    @SerialName("subcategory_ranking")
    val subcategoryRanking: String?,
    @SerialName("subtype")
    val subtype: List<Subtype>?,
    @SerialName("timezone")
    val timezone: String?,
    @SerialName("web_url")
    val webUrl: String?,
    @SerialName("website")
    val website: String?,
    @SerialName("write_review")
    val writeReview: String?
)

@Serializable
data class AddressObj(
    @SerialName("city")
    val city: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("postalcode")
    val postalcode: String?,
    @SerialName("state")
    val state: String?,
    @SerialName("street1")
    val street1: String?,
    @SerialName("street2")
    val street2: String?
)

@Serializable
data class Ancestor(
    @SerialName("abbrv")
    val abbrv: String?,
    @SerialName("location_id")
    val locationId: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("subcategory")
    val subcategory: List<SubcategoryX>?
)

@Serializable
data class Booking(
    @SerialName("provider")
    val provider: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class Category(
    @SerialName("key")
    val key: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class Hours(
    @SerialName("timezone")
    val timezone: String?,
    @SerialName("week_ranges")
    val weekRanges: List<List<WeekRange?>?>?
)

@Serializable
data class NearestMetroStation(
    @SerialName("address")
    val address: String?,
    @SerialName("distance")
    val distance: Double?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("lines")
    val lines: List<Line?>?,
    @SerialName("local_address")
    val localAddress: String?,
    @SerialName("local_name")
    val localName: String?,
    @SerialName("longitude")
    val longitude: Double?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class NeighborhoodInfo(
    @SerialName("location_id")
    val locationId: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class OfferGroup(
    @SerialName("has_see_all_url")
    val hasSeeAllUrl: Boolean?,
    @SerialName("is_eligible_for_ap_list")
    val isEligibleForApList: Boolean?,
    @SerialName("lowest_price")
    val lowestPrice: String?,
    @SerialName("lowest_ticket_price")
    val lowestTicketPrice: String?,
    @SerialName("offer_list")
    val offerList: List<Offer?>?,
    @SerialName("ticket_list")
    val ticketList: List<Ticket?>?
)

@Serializable
data class PhotoClass(
    @SerialName("caption")
    val caption: String?,
    @SerialName("helpful_votes")
    val helpfulVotes: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("images")
    val images: Images?,
    @SerialName("is_blessed")
    val isBlessed: Boolean?,
    @SerialName("published_date")
    val publishedDate: String?,
    @SerialName("uploaded_date")
    val uploadedDate: String?,
    @SerialName("user")
    val user: User?
)

@Serializable
data class RatingHistogram(
    @SerialName("count_1")
    val count1: String?,
    @SerialName("count_2")
    val count2: String?,
    @SerialName("count_3")
    val count3: String?,
    @SerialName("count_4")
    val count4: String?,
    @SerialName("count_5")
    val count5: String?
)

@Serializable
data class Review(
    @SerialName("author")
    val author: String?,
    @SerialName("machine_translated")
    val machineTranslated: Boolean?,
    @SerialName("published_date")
    val publishedDate: String?,
    @SerialName("published_platform")
    val publishedPlatform: String?,
    @SerialName("rating")
    val rating: String?,
    @SerialName("review_id")
    val reviewId: String?,
    @SerialName("summary")
    val summary: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class SubcategoryX(
    @SerialName("key")
    val key: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class Subtype(
    @SerialName("key")
    val key: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class WeekRange(
    @SerialName("close_time")
    val closeTime: Int?,
    @SerialName("open_time")
    val openTime: Int?
)

@Serializable
data class Line(
    @SerialName("line_name")
    val lineName: String?,
    @SerialName("line_symbol")
    val lineSymbol: String?,
    @SerialName("lineid")
    val lineid: String?,
    @SerialName("system_name")
    val systemName: String?,
    @SerialName("system_symbol")
    val systemSymbol: String?,
    @SerialName("type")
    val type: String?
)

@Serializable
data class Offer(
    @SerialName("description")
    val description: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("offer_type")
    val offerType: String?,
    @SerialName("partner")
    val partner: String?,
    @SerialName("price")
    val price: String?,
    @SerialName("primary_category")
    val primaryCategory: String?,
    @SerialName("product_code")
    val productCode: String?,
    @SerialName("rounded_up_price")
    val roundedUpPrice: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class Ticket(
    @SerialName("description")
    val description: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("offer_type")
    val offerType: String?,
    @SerialName("partner")
    val partner: String?,
    @SerialName("price")
    val price: String?,
    @SerialName("primary_category")
    val primaryCategory: String?,
    @SerialName("product_code")
    val productCode: String?,
    @SerialName("rounded_up_price")
    val roundedUpPrice: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class Images(
    @SerialName("large")
    val large: Large?,
    @SerialName("medium")
    val medium: MediumClass?,
    @SerialName("original")
    val original: OriginalClass?,
    @SerialName("small")
    val small: SmallClass?,
    @SerialName("thumbnail")
    val thumbnail: ThumbnailClass?
)

@Serializable
data class User(
    @SerialName("member_id")
    val memberId: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("user_id")
    val userId: String?
)

@Serializable
data class Large(
    @SerialName("height")
    val height: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("width")
    val width: String?
)

@Serializable
data class MediumClass(
    @SerialName("height")
    val height: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("width")
    val width: String?
)

@Serializable
data class OriginalClass(
    @SerialName("height")
    val height: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("width")
    val width: String?
)

@Serializable
data class SmallClass(
    @SerialName("height")
    val height: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("width")
    val width: String?
)

@Serializable
data class ThumbnailClass(
    @SerialName("height")
    val height: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("width")
    val width: String?
)

