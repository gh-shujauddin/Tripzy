package com.qadri.tripzy.presentation.placeDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GetDetailModel
import com.qadri.tripzy.domain.GetPlacePhotos
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.presentation.search.SearchDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripzyRepository: TripzyRepository
) : ViewModel() {

    private val placeId: Int = checkNotNull(savedStateHandle[PlaceDetailDestination.placeIdArg])

    private val _placeState = MutableStateFlow<ApiResult<GetDetailModel>>(ApiResult.Loading())
    val placeState = _placeState.asStateFlow()

    private val _placePhotoState = MutableStateFlow<ApiResult<GetPlacePhotos>>(ApiResult.Loading())
    val placePhotoState = _placePhotoState.asStateFlow()

    init {
        println(placeId)
        fetchPlaceDetail()
        getPlacePhotos()
    }
    private fun fetchPlaceDetail() {
        viewModelScope.launch {
            tripzyRepository.getPlaceDetail(placeId)
                .flowOn(Dispatchers.Default)
                .catch {
                    _placeState.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect {
                    _placeState.value = it
                }

        }
    }

    private fun getPlacePhotos() {
        viewModelScope.launch {
            tripzyRepository.getPlacePhotos(placeId)
                .flowOn(Dispatchers.Default)
                .catch {
                    _placePhotoState.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect {
                    _placePhotoState.value = it
                }
        }
    }

}