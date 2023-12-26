package com.qadri.tripzy.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.Place
import com.qadri.tripzy.constants.places
import com.qadri.tripzy.utils.ExpandableText
import kotlin.math.absoluteValue

@Composable
fun PlaceDetailScreen(

) {
    val item = places[0]
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            PhotoCarousel()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TitleComponent(place = item)
                Features(place = item)
                Description(place = item)
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }

}

@Composable
fun Features(place: Place) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoCarousel() {
    val place = places[0].link
    val pagerState = rememberPagerState(
        pageCount = { place.size }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        HorizontalPager(
            state = pagerState
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
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
                                .data(place[page])
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
                Box(
                    modifier = Modifier
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.padding(3.dp)
                    )
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

@Composable
fun TitleComponent(place: Place) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = place.name, style = MaterialTheme.typography.displayLarge)
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = "BookMark",
                modifier = Modifier
                    .width(25.dp)
                    .height(20.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = place.locationName, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "$${place.budgetPerDay}/day", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun Description(place: Place) {
    Column {
        ExpandableText(text = place.description,
            style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun PlaceDetailPrev() {
    Description(place = places[0])
}