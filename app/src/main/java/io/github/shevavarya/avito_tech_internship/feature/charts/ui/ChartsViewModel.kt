package io.github.shevavarya.avito_tech_internship.feature.charts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shevavarya.avito_tech_internship.core.model.CustomException
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.feature.charts.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.charts.ui.model.ChartsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChartsViewModel(private val chartsInteractor: ChartsInteractor) : ViewModel() {

    private val _state = MutableStateFlow<ChartsState>(ChartsState.Init)
    val state: StateFlow<ChartsState> = _state.asStateFlow()

    private var cachedCharts: List<Track>? = null
    var lastSearchQuery: String? = null

    init {
        getTrackChart()
    }

    fun getTrackChart() {
        viewModelScope.launch {
            chartsInteractor.getTrackChart()
                .onSuccess {
                    _state.emit(ChartsState.Content(it, true))
                    cachedCharts = it
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    fun searchTracks(query: String) {
        val queryText = query.trim()

        if (queryText.isEmpty()) {
            cachedCharts?.let {
                _state.value = (ChartsState.Content(it, true))
            }
            return
        }

        lastSearchQuery = queryText

        viewModelScope.launch {
            chartsInteractor.searchTracks(query)
                .onSuccess {
                    _state.emit(ChartsState.Content(it, false))
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    fun setLoading() {
        _state.value = ChartsState.Loading
    }

    private suspend fun handleError(error: Throwable) {
        when (error) {
            is CustomException.RequestError, CustomException.EmptyError -> _state.emit(ChartsState.EmptyError)
            CustomException.NetworkError -> _state.emit(ChartsState.NetworkError)
            CustomException.ServerError -> _state.emit(ChartsState.ServerError)
        }
    }

}