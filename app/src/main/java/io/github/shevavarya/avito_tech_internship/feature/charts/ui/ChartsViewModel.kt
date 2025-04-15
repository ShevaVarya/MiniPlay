package io.github.shevavarya.avito_tech_internship.feature.charts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shevavarya.avito_tech_internship.core.model.CustomException
import io.github.shevavarya.avito_tech_internship.feature.charts.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.charts.ui.model.ChartsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChartsViewModel(private val chartsInteractor: ChartsInteractor) : ViewModel() {

    private val _state = MutableStateFlow<ChartsState>(ChartsState.Loading)
    val state: StateFlow<ChartsState> = _state.asStateFlow()

    init {
        getTrackChart()
    }

    fun getTrackChart() {
        viewModelScope.launch {
            chartsInteractor.getTrackChart()
                .onSuccess {
                    _state.emit(ChartsState.Content(it))
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            chartsInteractor.searchTracks(query)
                .onSuccess {
                    _state.emit(ChartsState.Content(it))
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    private suspend fun handleError(error: Throwable) {
        when (error) {
            is CustomException.RequestError, CustomException.EmptyError -> _state.emit(ChartsState.EmptyError)
            CustomException.NetworkError -> _state.emit(ChartsState.NetworkError)
            CustomException.ServerError -> _state.emit(ChartsState.ServerError)
        }
    }

}