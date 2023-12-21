package com.qadri.tripzy.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Mic
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.data.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation

object SearchDestination : NavigationDestination {
    override val route: String = "search"
    override val titleRes: Int = R.string.search
    const val isFocusedArg = "isFocused"
    val routeWithArgs = "$route/{$isFocusedArg}"
}

@Composable
fun SearchScreen(
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Search),
    navController : NavController,
    onBottomItemClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    val isFocused = viewModel.uiState.collectAsState().value

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
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 12.dp, end = 12.dp, top = it.calculateTopPadding())
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = {
                    searchQuery = it
                },
                onSearch = {
                    viewModel.changeFocus(false)
                },
                isSearchActive = isFocused,
                onActiveChange = { focus ->
                    viewModel.changeFocus(focus)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        androidx.compose.material3.SearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = isSearchActive,
            onActiveChange = onActiveChange,
            shape = MaterialTheme.shapes.small,
            colors = SearchBarDefaults.colors(
                containerColor = Color.White,
                inputFieldColors = TextFieldDefaults.colors(
                    cursorColor = Color.Black
                )
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Sharp.Mic,
                    contentDescription = "Mic",
                    modifier = Modifier.size(24.dp)
                )
            }
        ) {

        }
    }

}

