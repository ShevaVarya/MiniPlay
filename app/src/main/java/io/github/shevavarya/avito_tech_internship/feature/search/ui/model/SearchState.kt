package io.github.shevavarya.avito_tech_internship.feature.search.ui.model

import io.github.shevavarya.avito_tech_internship.core.model.domain.Track

sealed interface SearchState {
    data object Init: SearchState
    data object Loading : SearchState
    data object EmptyError : SearchState
    data object ServerError : SearchState
    data object NetworkError : SearchState
    data class Content(val tracks: List<Track>, val isCached: Boolean) : SearchState
}