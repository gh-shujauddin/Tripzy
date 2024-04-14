package com.qadri.tripzy.presentation.plan

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.data.TripsEntity
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation
import com.qadri.tripzy.presentation.plan.tripDetails.TripDetailDestination
import com.qadri.tripzy.ui.theme.dark
import com.qadri.tripzy.ui.theme.white
import com.qadri.tripzy.utils.base64ToByteArray
import com.qadri.tripzy.utils.convertImageByteArrayToBitmap
import kotlinx.coroutines.flow.collectLatest

object PlanDestination : NavigationDestination {
    override val route: String
        get() = "plan"
    override val titleRes: Int
        get() = R.string.plan
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlanScreen(
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Plan),
    navController: NavController,
    onBottomItemClick: (Int) -> Unit,
    planViewModel: PlanViewModel
) {
    val sheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden, skipPartiallyExpanded = false
        )
    )
    val state = planViewModel.imageState.collectAsState()
    BottomSheetScaffold(
        sheetContent = {

        },
        sheetContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
        scaffoldState = modalSheetStates,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
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
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(top = it.calculateTopPadding())
                            .fillMaxSize()
                    ) {
                        val newItems = remember {
                            mutableStateListOf<TripInfo?>()
                        }
                        LaunchedEffect(key1 = planViewModel.allTrips) {
                            planViewModel.allTrips.collectLatest {
                                if (it.isNotEmpty()) {
                                    newItems.clear()
                                    newItems.addAll(extractTripInfo(it))
                                }
                            }
                        }

                        val scroll = rememberLazyListState()
                        Column {
                            LazyColumn(state = scroll) {
                                item {

                                    Row(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background)
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp, horizontal = 20.dp)
                                            .graphicsLayer {
                                                translationY =
                                                    -scroll.firstVisibleItemIndex.toFloat() / 2f
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontSize = 32.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            ) {
                                                append("Plan your trip ")
                                            }
                                        }, modifier = Modifier.padding(end = 10.dp))
                                    }
                                }
                                item {
                                    NewRouteCard(sheetState, navController)
                                }
                                items(newItems.reversed()) { item ->
                                    RouteCard(item, navController, planViewModel)
                                }
                                item {
                                    Spacer(modifier = Modifier.height(60.dp))
                                }
                            }
                        }
                    }

                    if (planViewModel.isAnimationVisible.value) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary.copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                when (state.value) {
                                    is ApiState.Error -> {
                                        Log.d(
                                            "API ERROR",
                                            (state.value as ApiState.Error).exception.toString()
                                        )
                                    }

                                    is ApiState.Loaded -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("location.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )

                                        Text(
                                            text = "Fetching your location",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }

                                    ApiState.Loading -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("location.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )

                                        Text(
                                            text = "Fetching your location",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }

                                    ApiState.NotStarted -> {
                                        Log.d(
                                            "API NOT STArted",
                                            (state.value as ApiState.Error).exception.toString()
                                        )
                                    }

                                    ApiState.ReceivedGeoCodes -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("itineary.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )
                                        Text(
                                            text = "Planning Itinerary",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }

                                    ApiState.ReceivedPhoto -> {
                                        planViewModel.isAnimationVisible.value = false
                                    }

                                    ApiState.ReceivedPhotoId -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("getset.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )
                                        Text(
                                            text = "Get Set Go",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }

                                    ApiState.ReceivedPlaceId -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("onyourmark.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )
                                        Text(
                                            text = "ON your Mark",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }

                                    ApiState.CalculatedDistance -> {
                                        val currenanim by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("go.json")
                                        )
                                        LottieAnimation(
                                            composition = currenanim,
                                            iterations = Int.MAX_VALUE,
                                            contentScale = ContentScale.Crop,
                                            speed = 1f,
                                            modifier = Modifier.size(250.dp)
                                        )
                                        Text(
                                            text = "Calculating Distance",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
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
fun NewRouteCard(sheetState: BottomSheetScaffoldState, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null,
                onClick = {
                    navController.navigate(NewTripDestination.route)
                }),
        colors = CardDefaults.cardColors(
            containerColor = white
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(0.8f),
                colors = CardDefaults.cardColors(
                    containerColor = white
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.route),
                        contentDescription = "",
                        modifier = Modifier.size(70.dp),
                        tint = dark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add New Route",
                fontSize = 15.sp,
                color = dark,
                modifier = Modifier.weight(1f)
            )

        }
    }
}

@Composable
fun RouteCard(
    item: TripInfo?,
    navController: NavController,
    planViewModel: PlanViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null,
                onClick = {
                    planViewModel.currentDestination.value = item?.name ?: ""
                    navController.navigate(TripDetailDestination.route)
                }),
        colors = CardDefaults.cardColors(
            containerColor = white
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(0.8f),
                colors = CardDefaults.cardColors(
                    containerColor = white
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item?.photoBase64?.let {
                        convertImageByteArrayToBitmap(base64ToByteArray(it))?.asImageBitmap()
                            ?.let { it1 ->
                                Image(
                                    bitmap = it1,
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item?.name ?: "",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

data class TripInfo(
    val name: String?,
    val photoBase64: String?,
    val budget: String?
)

fun extractTripInfo(items: List<TripsEntity?>): List<TripInfo> {
    val uniqueNamesMap = items.groupBy { it?.destination }

    return uniqueNamesMap.map { entry ->
        val firstMatchNotNull =
            entry.value.firstOrNull { it?.photoBase64 != null && it.budget != null }
        TripInfo(
            name = entry.value.firstOrNull()?.destination,
            photoBase64 = firstMatchNotNull?.photoBase64,
            budget = firstMatchNotNull?.budget
        )
    }
}
