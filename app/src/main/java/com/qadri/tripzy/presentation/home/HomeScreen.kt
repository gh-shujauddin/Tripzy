package com.qadri.tripzy.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.SliderList
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.constants.categoryList
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.DataClass
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation
import com.qadri.tripzy.presentation.placeDetail.PlaceDetailDestination
import com.qadri.tripzy.utils.CardsRow
import com.qadri.tripzy.utils.NonlazyGrid
import kotlin.math.absoluteValue

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Explore),
    onSearchClick: (Boolean) -> Unit,
    onBottomItemClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    val apiResult = homeViewModel.recommendedPlace.collectAsState().value
    val rankingApiResult = homeViewModel.rankingState.collectAsState().value

    var rankingPlace = listOf<DataClass>()
    var recommendedPlace = listOf<DataClass>()

    when (apiResult) {
        is ApiResult.Success -> {
            recommendedPlace = apiResult.data?.data!!
        }

        else -> {

        }
    }

    when (rankingApiResult) {
        is ApiResult.Success -> {
            rankingPlace = rankingApiResult.data?.data!!
        }

        else -> {

        }
    }
    var isCategoryClicked by remember {
        mutableStateOf(false)
    }
    Scaffold(
        bottomBar = {
            TripzyBottomNavigation(
                navController = navController,
                list = bottomNavigationItems,
                defaultSelectedIndex = defaultSelectedIndex,
                onBottomItemClick = onBottomItemClick
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 12.dp, end = 12.dp, top = it.calculateTopPadding())
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Header()
            }
            item {
                SearchBarMock(onClick = {
                    onSearchClick(true)
                })
                Spacer(modifier = Modifier.height(24.dp))

            }
            item {
                Carousel(
                    sliderList = recommendedPlace,
                    onItemClick = onItemClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CategoriesComp(
                    onCardClick = {
//                        isCategoryClicked = true
                    }
                )
//                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(visible = isCategoryClicked) {
                    CardsRow()
                }
                Spacer(modifier = Modifier.height(34.dp))
            }
            item {
                TopTrips(sliderList = rankingPlace, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun SearchBarMock(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .height(48.dp)
            .clickable {
                onClick()
            }
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "search",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun Header() {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(start = 6.dp)) {
        Text(text = "Discover", style = MaterialTheme.typography.titleLarge)
        Text(text = "The beauty today", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    Header()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    sliderList: List<DataClass>,
    onItemClick: (Int) -> Unit
) {

//    val sliderList = placesList
    val pagerState = rememberPagerState(
        pageCount = { sliderList.size }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(MaterialTheme.shapes.extraLarge)
    ) {
        HorizontalPager(
            state = pagerState
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
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
                Box {
                    val imageLink = sliderList[page].photo?.images?.original?.url
                    println(imageLink)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageLink)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.lake),
                        error = painterResource(id = R.drawable.ic_error_image_generic),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        //Texts
                        sliderList[page].name?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                        sliderList[page].address?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White

                            )
                        }
                    }
                    Column(modifier = Modifier.fillMaxHeight()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ) {

                            Button(
                                onClick = { onItemClick(sliderList[page].location_id.toInt()) },
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(text = "Explore")
                                Icon(
                                    imageVector = Icons.Filled.ArrowRightAlt,
                                    contentDescription = "Explore"
                                )
                            }
                        }
                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
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
                                )
                            }
                        }
                    }
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesComp(
    onCardClick: () -> Unit
) {
    val categories = categoryList
    Column {
        Text(text = "Categories", style = MaterialTheme.typography.displayLarge)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            items(categories) { item ->
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(horizontal = 4.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    onClick = onCardClick
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier.clip(MaterialTheme.shapes.extraLarge)
                        ) {
                            Image(
                                painter = painterResource(id = item.imgResId),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        Text(text = item.title, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun TopTrips(
    sliderList: List<DataClass>,
    onItemClick: (Int) -> Unit
) {
    val categories = sliderList
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Top Trips", style = MaterialTheme.typography.displayLarge)
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Explore", style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = Icons.Filled.ArrowRightAlt,
                    contentDescription = "Explore"
                )
            }
        }

        NonlazyGrid(columns = 2, itemCount = categories.size) {
            TopTripCard(
                item = categories[it],
                onItemClick = {
                    onItemClick(categories[it].location_id.toInt())
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTripCard(
    item: DataClass,
    onItemClick: () -> Unit
) {
    Card(
        onClick = {
            onItemClick()
        },
        modifier = Modifier
            .padding(4.dp)
            .height(320.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.photo?.images?.original?.url)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.lake),
                    error = painterResource(id = R.drawable.ic_error_image_generic),
                    modifier = Modifier.height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column {
                    Text(text = item.name ?: "", style = MaterialTheme.typography.titleMedium)
                    Row(modifier = Modifier) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 4.dp, end = 4.dp)
                                .size(20.dp)
                        )
                        Text(text = item.address ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

