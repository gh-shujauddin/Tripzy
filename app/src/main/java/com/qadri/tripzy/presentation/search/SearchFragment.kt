package com.qadri.tripzy.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.qadri.tripzy.R
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.presentation.navigation.NavigationDestination

object SearchFragmentDestination: NavigationDestination{
    override val route: String = "search_fragment"
    override val titleRes: Int = R.string.search_fragment
}
@Composable
fun SearchFragment() {
    var query by remember {
        mutableStateOf("")
    }
    val searchViewModel: SearchViewModel = hiltViewModel()
    val apiResult =
        searchViewModel.searchAutoComplete.collectAsState().value

//    SearchBarMUI(
//        searchQuery = query,
//        onQueryChange = {query = it},
//        isSearchActive = true,
//        onSearch = {},
//        onActiveChange = {} ,
//        apiResult = apiResult
//    )
}