package com.qadri.tripzy.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qadri.tripzy.R
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GeoCode
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Result
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.search.RecentSearches
import com.qadri.tripzy.presentation.search.SearchCard
import com.qadri.tripzy.presentation.search.SearchViewModel
import com.qadri.tripzy.utils.LoadingCircularProgressIndicator
import kotlinx.coroutines.launch

object AskDetailDestination : NavigationDestination {
    override val route: String
        get() = "ask_user_detail"
    override val titleRes: Int
        get() = R.string.user_details

}

@Composable
fun AskDetailScreen(
    navigateToHome: () -> Unit
) {
    var isSearching by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val viewModel: RegisterViewModel = hiltViewModel()

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var residence by rememberSaveable {
        mutableStateOf("")
    }

    var latitude by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }
    var longitude by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }
    val state = viewModel.saveUserState.collectAsState(initial = null)


    val focusManager = LocalFocusManager.current
    if (isSearching) {
        val apiResult =
            viewModel.searchAutoComplete.collectAsState().value

        SearchBarMUI(
            searchQuery = residence,
            onQueryChange = {
                residence = it
                viewModel.fetchSearchAutoComplete(residence)
            },
            isSearchActive = true,
            onSearch = {},
            onActiveChange = {},
            apiResult = apiResult,
            onCardClick = { result ->
                latitude = result.detailsV2?.geocode?.latitude!!
                longitude = result.detailsV2.geocode.longitude
                viewModel.saveUser(name = name, latitude = latitude, longitude = longitude)
                navigateToHome()
            }
        )

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .size(70.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                //App Icon
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Let's get the basics so we can give you the goods.",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = stringResource(id = R.string.name),
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                maxLines = 1,
                textStyle = MaterialTheme.typography.displaySmall,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {  /*on Done Logic*/
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            OutlinedTextField(
                value = residence,
                onValueChange = {
                    residence = it
                    focusManager.clearFocus()
                    isSearching = true
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.residence),
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                maxLines = 1,
                textStyle = MaterialTheme.typography.displaySmall,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {  /*on Done Logic*/
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        viewModel.addUserDetail(name, latitude, longitude)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (state.value?.isLoading == true) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                } else {
                    Text(
                        text = stringResource(id = R.string.submit),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarMUI(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    recentSearches: List<Recents> = listOf(),
    onCardClick: (Result) -> Unit = {},
    apiResult: ApiResult<LocationResult>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.SearchBar(modifier = Modifier
            .defaultMinSize(
                minWidth = SearchBarDefaults.InputFieldHeight,
                minHeight = 48.dp
            )
            .fillMaxWidth(),
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = isSearchActive,
            onActiveChange = onActiveChange,
            shape = MaterialTheme.shapes.small,
            colors = SearchBarDefaults.colors(
                containerColor = Color.White, inputFieldColors = TextFieldDefaults.colors(
                    cursorColor = Color.Black
                )
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            }
        ) {
//
//            RecentSearches(
//                recentListFromDb = recentSearches,
//                onCardClick = onCardClick,
//                onLongCardClick = {})

            when (apiResult) {
                is ApiResult.Loading -> {
                    if (searchQuery.isNotBlank()) {
                        LoadingCircularProgressIndicator()

                    }
                }

                is ApiResult.Error -> {
                    Toast.makeText(LocalContext.current, apiResult.error, Toast.LENGTH_SHORT).show()
                }

                is ApiResult.Success -> {
                    val locationResultList =
                        apiResult.data?.data?.Typeahead_autocomplete?.results?.filter {
                            it.detailsV2?.placeType == "CITY"
                        } ?: listOf()
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(locationResultList) { result ->
                            if (result.image != null && result.detailsV2 != null) {

                                SearchCard(result = result, onCardClick = { onCardClick(result) })
                            }
                        }
                    }
                }
            }
        }
    }
}