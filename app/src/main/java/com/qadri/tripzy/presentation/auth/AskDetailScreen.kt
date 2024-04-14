package com.qadri.tripzy.presentation.auth

import android.Manifest
import android.graphics.Bitmap
import android.location.Location
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.qadri.tripzy.R
import com.qadri.tripzy.data.LocationManager
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GeoCode
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.domain.Result
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.search.RecentSearches
import com.qadri.tripzy.presentation.search.SearchCard
import com.qadri.tripzy.presentation.search.SearchViewModel
import com.qadri.tripzy.ui.ApplicationViewModel
import com.qadri.tripzy.utils.LoadingCircularProgressIndicator
import kotlinx.coroutines.launch
import kotlin.random.Random

object AskDetailDestination : NavigationDestination {
    override val route: String
        get() = "ask_user_detail"
    override val titleRes: Int
        get() = R.string.user_details

}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun AskDetailScreen(
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val viewModel: RegisterViewModel = hiltViewModel()
    val applicationViewModel: ApplicationViewModel = hiltViewModel()

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var residence by rememberSaveable {
        mutableStateOf("")
    }

    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val locationPermissionsState = rememberMultiplePermissionsState(
        locationPermissions
    )
    val activity = (context as ComponentActivity)
    val locationManager = LocationManager(context, activity)
    val isGpsEnabled = locationManager.gpsStatus.collectAsState(initial = false)
    val locationState =
        applicationViewModel.locationFlow.collectAsState(initial = newLocation())

    val updateDetailsStatus = viewModel.updateDetailsStatus.collectAsState(initial = null)

    val focusManager = LocalFocusManager.current
    var selectedImage by remember {
        mutableStateOf<ByteArray?>(null)
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImage = handleImageSelection(uri, context)
            }
        }

    val requestCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                selectedImage = handleImageCapture(it)
            }
        }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    LaunchedEffect(key1 = updateDetailsStatus.value?.isSuccess) {
        scope.launch {
            if (updateDetailsStatus.value?.isSuccess != null) {
                val success = updateDetailsStatus.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                navigateToHome()
            }
        }
    }

    LaunchedEffect(key1 = updateDetailsStatus.value?.isError) {
        scope.launch {
            if (updateDetailsStatus.value?.isError?.isNotEmpty() == true) {
                val error = updateDetailsStatus.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = WindowInsets.ime
            ) {
                // Sheet content
                PhotoPickerOptionBottomSheet(onGalleryClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                    galleryLauncher.launch("image/*")
                }, onCameraClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                    if (!cameraPermission.status.isGranted) {
                        cameraPermission.launchPermissionRequest()
                    }
                    if (cameraPermission.status.isGranted) {
                        requestCameraLauncher.launch(null)
                    }
                })
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row (Modifier.fillMaxWidth()){
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .size(70.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                //App Icon
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Let's get the basics so we can give you the goods.",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .border(BorderStroke(2.dp, Color.Black), CircleShape)
                .size(100.dp)
                .clickable {
                    showBottomSheet = true
                }
        ) {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                    modifier = Modifier.padding(4.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (selectedImage == null) {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = "Add photo",
                        tint = Color.Gray,
                        modifier = Modifier.size(70.dp)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .crossfade(true)
                            .data(selectedImage).build(),
                        contentDescription = "Selected image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {  /*on Done Logic*/
                    focusManager.moveFocus(FocusDirection.Down)
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
            },
            label = {
                Text(
                    text = "Address",
                    style = MaterialTheme.typography.displaySmall
                )
            },
            trailingIcon = {

                Icon(
                    imageVector = Icons.Outlined.MyLocation,
                    contentDescription = "Get location",
                    modifier = Modifier.clickable {
                        if (locationPermissionsState.allPermissionsGranted) {
                            if (!isGpsEnabled.value) {
                                locationManager.checkGpsSettings()
                                println("Not enabled")
                            } else {
                                residence = locationManager.getAddressFromCoordinate(
                                    latitude = locationState.value.latitude,
                                    longitude = locationState.value.longitude
                                )
                                println("enabled")

                            }
                        } else {
                            println("Permisson not got")

                            locationPermissionsState.launchMultiplePermissionRequest()
                        }

                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.displaySmall,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {  /*on Done Logic*/
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)

        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    if (name.isNotEmpty() || residence.isNotEmpty() || selectedImage == null) {
                        viewModel.addUserDetail(name, residence, selectedImage!!)
                    } else {
                        Toast.makeText(context, "Empty fields", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (updateDetailsStatus.value?.isLoading == true) {
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


@Composable
fun PhotoPickerOptionBottomSheet(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clickable {
                    onCameraClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraEnhance,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )

            Text(
                text = "Camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    onGalleryClick()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Image, contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )
            Text(
                text = "Gallery",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
    }
}


private fun newLocation(): Location {
    val location = Location("MyLocationProvider")
    location.apply {
        latitude = 27.509865 + Random.nextFloat()
        longitude = 78.118092 + Random.nextFloat()
    }
    return location
}