package com.qadri.tripzy.presentation.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.Recents
import com.qadri.tripzy.data.TripzyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isFocused: Boolean,
    val searchQuery: String,
//    val recentSearches: List<String>,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripzyRepository: TripzyRepository
) : ViewModel() {
    private val isFocused: Boolean = checkNotNull(savedStateHandle[SearchDestination.isFocusedArg])

    val recentSearches =
        tripzyRepository.getAllRecentSearch()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                listOf<Recents>()
            )


    private val searchQuery = ""

    private val _uiState = MutableStateFlow(UiState(isFocused, searchQuery))
    val uiState = _uiState.asStateFlow()

    private val _searchAutoComplete = MutableStateFlow<ApiResult<LocationResult>>(ApiResult.Loading())
    val searchAutoComplete = _searchAutoComplete.asStateFlow()

    private fun fetchSearchAutoComplete() {
        viewModelScope.launch {
            tripzyRepository.getSearchAutoComplete(string = _uiState.value.searchQuery)
                .flowOn(Dispatchers.Default)
                .catch {
                    _searchAutoComplete.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect{
                    _searchAutoComplete.value = it
                }

        }
    }


    fun changeFocus(focus: Boolean) {
        _uiState.update {
            it.copy(isFocused = focus)
        }
    }

    fun onQueryChange(text: String) {
        _uiState.update {
            it.copy(searchQuery = text)
        }
        fetchSearchAutoComplete()
    }

    suspend fun updateRecentSearch() {
        Log.d("Recent", _uiState.value.searchQuery)
        viewModelScope.launch {
            tripzyRepository.addRecentSearch(Recents(0, _uiState.value.searchQuery))
        }
    }

    suspend fun deleteRecentSearch(recent: Recents) {
        viewModelScope.launch {
            tripzyRepository.deleteRecentSearch(recent)
        }
    }
}