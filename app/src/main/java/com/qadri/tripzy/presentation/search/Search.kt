package com.qadri.tripzy.presentation.search

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.Cities
import com.qadri.tripzy.constants.City
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Result
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation
import com.qadri.tripzy.utils.AlertDialog
import com.qadri.tripzy.utils.CardsRow
import com.qadri.tripzy.utils.LoadingCircularProgressIndicator
import com.qadri.tripzy.utils.NonlazyGrid
import com.qadri.tripzy.utils.SearchBar
import kotlinx.coroutines.launch

object SearchDestination : NavigationDestination {
    override val route: String = "search"
    override val titleRes: Int = R.string.search
    const val isFocusedArg = "isFocused"
    val routeWithArgs = "$route/{$isFocusedArg}"
}

@Composable
fun SearchScreen(
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Search),
    navController: NavController,
    onBottomItemClick: (Int) -> Unit,
    onCardClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val searchQuery = viewModel.uiState.collectAsState().value.searchQuery
    val isFocused = viewModel.uiState.collectAsState().value.isFocused
//    val recentSearches = viewModel.uiState.collectAsState().value.recentSearches
    val recentSearches = viewModel.recentSearches.collectAsState().value
    val apiResult =
        viewModel.searchAutoComplete.collectAsState().value
    val locationResultList =
        apiResult.data?.data?.Typeahead_autocomplete?.results ?: listOf()

    Scaffold(bottomBar = {
        TripzyBottomNavigation(
            navController = navController,
            list = bottomNavigationItems,
            defaultSelectedIndex = defaultSelectedIndex,
            onBottomItemClick = onBottomItemClick
        )
    }) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 12.dp, end = 12.dp, top = it.calculateTopPadding())
        ) {

            var openAlertDialog by remember { mutableStateOf(false) }
            var openAlertDialogItem by remember {
                mutableStateOf(Recents(0, ""))
            }
            // ...
            when {
                // ...
                openAlertDialog -> {
                    AlertDialog(
                        onDismissRequest = { openAlertDialog = false },
                        onConfirmation = {
                            openAlertDialog = false
                            scope.launch {
                                if (openAlertDialogItem.recentResult.isNotEmpty())
                                    viewModel.deleteRecentSearch(openAlertDialogItem)
                            }
                            println("Confirmation registered") // Add logic here to handle confirmation.
                        },
                        dialogTitle = "Deleting confirmation",
                        dialogText = "Are you sure to delete ${openAlertDialogItem.recentResult} from recent searches?"
                    )
                }
            }
//            SearchBar(
//                searchQuery = searchQuery,
//                onQueryChange = viewModel::onQueryChange,
//                onSearch = {
//                    viewModel.changeFocus(false)
//                },
//                isSearchActive = isFocused,
//                onActiveChange = { focus ->
//                    viewModel.changeFocus(focus)
//                },
//                recentSearches = recentSearches,
//                onCardClick = viewModel::onQueryChange,
//                apiResult = apiResult
//            )
            SearchBar(
                hint = "Places, attractions, etc",
                searchQuery = searchQuery,
                onSearchClicked = {
                    scope.launch {
                        viewModel.updateRecentSearch()
                    }
                    viewModel.changeFocus(false)
                },
                onTextChange = viewModel::onQueryChange,
                height = 40.dp
            )


            when (apiResult) {
                is ApiResult.Loading -> {
                    AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                        LoadingCircularProgressIndicator()
                    }
                }

                else -> {}
            }


            LazyColumn(modifier = Modifier.weight(1f)) {

                items(locationResultList) { result ->
                    when (apiResult) {
                        is ApiResult.Loading -> {
                            AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                                LoadingCircularProgressIndicator()
                            }
                        }

                        is ApiResult.Error -> {
                            Toast.makeText(
                                LocalContext.current,
                                apiResult.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ApiResult.Success -> {
                            if (result.image != null && result.detailsV2 != null) {
                                SearchCard(result = result, onCardClick = {
                                    result.detailsV2.locationId.let { it1 -> onCardClick(it1) }
                                })
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }

                item {
                    RecentSearches(
                        recentListFromDb = recentSearches,
                        onCardClick = viewModel::onQueryChange,
                        onLongCardClick = { recent ->
                            openAlertDialog = true
                            openAlertDialogItem = recent
                        }
                    )
//                    Headings(text = "Top Experiences")
//                    CardsRow()
                    CityCards()
                }
            }
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
fun SearchCard(
    result: Result,
    onCardClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { onCardClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small)
        ) {
            val lastIndex =
                result.image!!.photo.photoSizes.lastIndex ?: 0
            val listPhoto =
                result.image.photo.photoSizes[lastIndex].url

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(listPhoto)
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
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            result.detailsV2!!.names.name.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                result.detailsV2.names.longOnlyHierarchyTypeaheadV2.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                result.detailsV2.placeType.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.shapes.small
                            )
                            .width(90.dp)
                            .padding(horizontal = 5.dp),
                        maxLines = 1

                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentSearches(
    recentListFromDb: List<Recents>,
    onCardClick: (String) -> Unit,
    onLongCardClick: (Recents) -> Unit = {}
) {

    val recentList = recentListFromDb.reversed()
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Recently searched",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(40.dp)
        ) {
            items(recentList) { recent ->
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                        .combinedClickable(
                            onLongClick = { onLongCardClick(recent) },
                            onClick = { onCardClick(recent.recentResult) }
                        ),
//                    onClick = { onCardClick(recent.recentResult) },

                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = recent.recentResult,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun CityCards() {
    Headings(text = "Destinations traveller love")
    NonlazyGrid(columns = 2, itemCount = Cities.size) {
        CityCard(item = Cities[it])
    }
}

@Composable
fun CityCard(item: City) {
    Box(
        Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(1f)
            .background(Color.White)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.link)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = item.name,
            placeholder = painterResource(id = R.drawable.lake),
            error = painterResource(id = R.drawable.ic_error_image_generic),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun Headings(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}