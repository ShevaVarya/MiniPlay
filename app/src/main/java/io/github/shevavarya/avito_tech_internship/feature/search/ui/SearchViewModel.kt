package io.github.shevavarya.avito_tech_internship.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shevavarya.avito_tech_internship.core.component.MediaStoreManager
import io.github.shevavarya.avito_tech_internship.core.model.CustomException
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.feature.search.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.search.ui.model.Flag
import io.github.shevavarya.avito_tech_internship.feature.search.ui.model.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val chartsInteractor: ChartsInteractor,
    val musicManager: MediaStoreManager,
    private val args: SearchFragmentArgs
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Init)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var cachedTracks: List<Track>? = null
    var lastSearchQuery: String? = null

    init {
        if (args.mode == Flag.NETWORK) {
            getTrackChart()
        }
    }

    fun getTrackChart() {
        viewModelScope.launch {
            chartsInteractor.getTrackChart()
                .onSuccess {
                    _state.emit(SearchState.Content(it, true))
                    cachedTracks = it
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    fun getLocalTracks() {
        viewModelScope.launch {
            val tracks = musicManager.getLocalTracks()
            if (tracks.isEmpty()) {
                handleError(CustomException.EmptyError)
            } else {
                _state.emit(SearchState.Content(tracks, true))
                cachedTracks = tracks
            }
        }
    }

    fun searchTracks(query: String) {
        val queryText = query.trim()

        //Никогда не дойдет
        /*if (queryText.isEmpty()) {
            cachedTracks?.let {
                _state.value = (SearchState.Content(it, true))
            }
            return
        }*/

        lastSearchQuery = queryText
        when (args.mode) {
            Flag.LOCAL -> searchLocalTracks(queryText)
            Flag.NETWORK -> searchNetworkTracks(queryText)
        }
    }

    fun setCached() {
        cachedTracks?.let {
            _state.value = SearchState.Content(it, true)
        }
    }

    fun setLoading() {
        _state.value = SearchState.Loading
    }

    private fun searchNetworkTracks(query: String) {
        viewModelScope.launch {
            chartsInteractor.searchTracks(query)
                .onSuccess {
                    _state.emit(SearchState.Content(it, false))
                }
                .onFailure {
                    handleError(it)
                }
        }
    }

    private fun searchLocalTracks(query: String) {
        viewModelScope.launch {
            val tracks = cachedTracks?.filter { track ->
                track.title.contains(query, ignoreCase = true) ||
                        track.artist.name.contains(query, ignoreCase = true)
            }
            tracks?.let {
                if (tracks.isEmpty()) {
                    handleError(CustomException.EmptyError)
                } else {
                    _state.emit(SearchState.Content(tracks, false))
                }
            }
        }
    }

    private suspend fun handleError(error: Throwable) {
        when (error) {
            is CustomException.RequestError, CustomException.EmptyError -> _state.emit(SearchState.EmptyError)
            CustomException.NetworkError -> _state.emit(SearchState.NetworkError)
            CustomException.ServerError -> _state.emit(SearchState.ServerError)
        }
    }

}