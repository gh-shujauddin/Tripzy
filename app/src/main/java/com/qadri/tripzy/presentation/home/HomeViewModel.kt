package com.qadri.tripzy.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.GetRecommendedPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TripzyRepository
) : ViewModel() {

    private val _recommendedState =
        MutableStateFlow<ApiResult<GetRecommendedPlace>>(ApiResult.Loading())
    val recommendedPlace = _recommendedState.asStateFlow()

    private val _rankingState =
        MutableStateFlow<ApiResult<GetRecommendedPlace>>(ApiResult.Loading())
    val rankingState = _rankingState.asStateFlow()

    init {
        fetchRecommendedPlaces()
        fetchRankedPlaces()
    }

    private fun fetchRecommendedPlaces() {
        viewModelScope.launch {
            repository.getRecommendedPlace()
                .flowOn(Dispatchers.Default)
                .catch {
                    _recommendedState.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect {
                    _recommendedState.value = it
                }

        }
    }

    private fun fetchRankedPlaces() {
        viewModelScope.launch {
            repository.getRankedPlace()
                .flowOn(Dispatchers.Default)
                .catch {
                    _rankingState.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect {
                    _rankingState.value = it
                }
        }
    }
}