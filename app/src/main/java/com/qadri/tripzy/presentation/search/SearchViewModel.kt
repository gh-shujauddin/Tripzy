package com.qadri.tripzy.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val isFocused: Boolean = checkNotNull(savedStateHandle[SearchDestination.isFocusedArg])

    private val _uiState = MutableStateFlow(isFocused)
    val uiState = _uiState.asStateFlow()

    fun changeFocus(focus: Boolean) {
        _uiState.value = focus
    }
}