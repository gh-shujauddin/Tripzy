package com.qadri.tripzy.presentation.placeDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeOfTravel
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.qadri.tripzy.R
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GeoCode
import com.qadri.tripzy.domain.GetPlacePhotos
import com.qadri.tripzy.domain.NearestMetro
import com.qadri.tripzy.domain.Place
import com.qadri.tripzy.domain.Reviews
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.search.Headings
import com.qadri.tripzy.utils.ExpandableText
import com.qadri.tripzy.utils.LoadingCircularProgressIndicator
import java.util.Locale
import kotlin.math.absoluteValue


object PlaceDetailDestination : NavigationDestination {
    override val route: String = "place_detail"
    override val titleRes: Int = R.string.place_detail
    const val placeIdArg = "placeId"
    val routeWithArgs = "$route/{$placeIdArg}"
}


@Composable
fun PlaceDetailScreen(
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
//    val item = places[0]
    val placeDetailViewModel: PlaceDetailViewModel = hiltViewModel()
    val getPlaceStateApiResult = placeDetailViewModel.placeState.collectAsState().value
    val getPlacePhotoApiResult = placeDetailViewModel.placePhotoState.collectAsState().value

    val data = getPlaceStateApiResult.data

    val placeName = getPlaceStateApiResult.data?.name
    val ranking = data?.ranking
    val rating = data?.rating
    val description = data?.description
    var tags = setOf<String?>()
    val category = data?.category?.name
    tags = tags + category
    data?.subcategory?.forEach {
        tags = tags + it.name
    }
    val latitude = data?.latitude?.toDouble()
    val longitude = data?.longitude?.toDouble()
    val geoPoint =
        if (latitude != null && longitude != null)
            GeoCode(latitude = latitude, longitude = longitude)
        else
            GeoCode(0.0, 0.0)

    val isClosed = data?.isClosed
    val reviews = data?.reviews?.map {
        Reviews(
            title = it.title,
            rating = it.rating,
            publishedDate = it.publishedDate,
            summary = it.summary,
            author = it.author,
            url = it.url
        )
    }
    val phone = data?.phone
    val website = data?.website
    val email = data?.email

    val bookingLink = data?.booking
    val address = data?.address
    val nearestMetro = data?.nearestMetroStation?.map {
        NearestMetro(it.name, it.address)
    }
    val recommendedTripTime = data?.recommendedVisitLength


    when (getPlaceStateApiResult) {
        is ApiResult.Error -> {
            Toast.makeText(
                LocalContext.current,
                getPlaceStateApiResult.error,
                Toast.LENGTH_SHORT
            ).show()
        }

        is ApiResult.Loading -> {
            LoadingCircularProgressIndicator()
        }

        is ApiResult.Success -> {

            val place = Place(
                name = placeName,
                link = website,
                locationName = address,
                description = description,
                rating = rating?.toDouble(),
                totalReviews = reviews?.size,
                tags = tags,
                phone = phone,
                recommendedTripTime = recommendedTripTime,
                email = email,
                bookingLink = bookingLink?.url,
                ranking = ranking,
                nearestMetro = nearestMetro,
                isClosed = isClosed,
                reviews = reviews,
                geoPoint = geoPoint
            )
            Log.d("Place", "$place")
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    PhotoCarousel(
                        onNavigateUp = onNavigateUp,
                        getPlacePhotoApiResult = getPlacePhotoApiResult
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        TitleComponent(place = place)
                        Features(place = place)
                        place.description.let {
                            Description(place = place)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ExploreOnMap(geoPoint = geoPoint, context = context)
                        ReviewsComp(place = place)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreOnMap(geoPoint: GeoCode, context: Context) {
    Card(
        onClick = {
            val uri: String = java.lang.String.format(
                Locale.ENGLISH,
                "geo:%f,%f",
                geoPoint.latitude,
                geoPoint.longitude
            )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Text(text = "Explore on Maps")
        }
    }
}

@Composable
fun Features(place: Place) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))

            ) {
                Icon(
                    imageVector = Icons.Filled.Timelapse,
                    contentDescription = "",
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Suggested Duration", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = place.recommendedTripTime ?: "More than 2 hours",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FeaturesComp(
            imageVector = Icons.Filled.Star,
            title = "${place.rating}",
            text = "${place.totalReviews} rating"
        )
        FeaturesComp(
            imageVector = Icons.Filled.Cloud,
            title = "${place.averageTemperature}°C",
            text = "Temperature"
        )
        FeaturesComp(
            imageVector = Icons.Filled.Flag,
            title = "${place.distance}",
            text = "Distance"
        )
    }
    Divider()
}

@Composable
fun FeaturesComp(
    imageVector: ImageVector,
    title: String,
    text: String
) {
    Row(
        modifier = Modifier.padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .size(40.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Rating",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Text(text = text, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoCarousel(
    onNavigateUp: () -> Unit,
    getPlacePhotoApiResult: ApiResult<GetPlacePhotos>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        val photoLinks = getPlacePhotoApiResult.data?.data?.map {
            it.images.original.url
        } ?: listOf("")
        val place = photoLinks[0]
        val pagerState = rememberPagerState(
            pageCount = {
                photoLinks.size
            }
        )
        HorizontalPager(
            state = pagerState
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (getPlacePhotoApiResult) {
                    is ApiResult.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            getPlacePhotoApiResult.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ApiResult.Loading -> {
                        LoadingCircularProgressIndicator()
                    }

                    is ApiResult.Success -> {
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .graphicsLayer {
                                    val pageOffset = (
                                            (pagerState.currentPage - page) + pagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
                                    alpha = lerp(
                                        0.5f,
                                        1f,
                                        1 - pageOffset.coerceIn(0f, 1f)
                                    )
                                },
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {


                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photoLinks?.get(page))
                                        .crossfade(true)
                                        .scale(Scale.FILL)
                                        .build(),
                                    contentDescription = null,
                                    placeholder = painterResource(id = R.drawable.lake),
                                    error = painterResource(id = R.drawable.ic_error_image_generic),
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }


                        }
                        Card(
                            onClick = onNavigateUp,
                            modifier = Modifier
                                .padding(20.dp),
                            colors = CardDefaults.cardColors(Color.Transparent.copy(alpha = 0.4f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = "Back",
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                            .shadow(1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TitleComponent(place: Place) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            place.name?.let { Text(text = it, style = MaterialTheme.typography.displayLarge) }
//            Icon(
//                imageVector = Icons.Filled.Bookmark,
//                contentDescription = "BookMark",
//                modifier = Modifier
//                    .width(25.dp)
//                    .height(20.dp)
//            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            RatingBar(currentRating = place.rating?.toInt() ?: 0, starsColor = MaterialTheme.colorScheme.tertiary)

        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = place.ranking ?: "", style = MaterialTheme.typography.bodyMedium)

        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Filled.Public, contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            place.tags.forEach {
                Text(text = ("$it • "), style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                place.locationName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
//            Text(
//                text = "$${place.budgetPerDay}/day",
//                style = MaterialTheme.typography.bodyMedium
//            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        IsOpened(isClosed = place.isClosed ?: false, webUrl = place.link ?: "")
    }
}

@Composable
fun Description(place: Place) {
    Column {
        Headings(text = "About")
        ExpandableText(
            text = place.description ?: "Description will be updated soon.",
            style = MaterialTheme.typography.bodyMedium
        )

    }
}


@Composable
fun IsOpened(isClosed: Boolean, webUrl: String) {
    val localUriHandler = LocalUriHandler.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    localUriHandler.openUri(webUrl)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (!isClosed) {
                Text(
                    text = "Open Now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF006400)
                )
            } else {
                Text(
                    text = "Closed Now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "Go")
        }
    }
}


@Composable
fun ReviewsComp(
    place: Place
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Headings(text = "Reviews")
        place.reviews?.forEach { reviews ->
            Review(reviews)
        }
    }

}

@Composable
fun Review(
    reviews: Reviews
) {
//    Log.d("REVIES", "$reviews")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                reviews.url
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "${reviews.author}", style = MaterialTheme.typography.displaySmall)
        }
        Row {
            reviews.rating?.toInt()?.let {
                RatingBar(
                    currentRating = it,
                    starsColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Text(text = "${reviews.title}", style = MaterialTheme.typography.titleSmall)
        ExpandableText(
            text = "${reviews.summary}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Written ${reviews.publishedDate?.subSequence(0, 10)}",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    currentRating: Int,
    starsColor: Color = Color.Yellow
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Filled.Star
                else Icons.Filled.StarOutline,
                contentDescription = null,
                tint = if (i <= currentRating) starsColor
                else Color.Unspecified,
                modifier = Modifier
                    .padding(2.dp)
            )
        }
    }
}
