package io.github.shevavarya.avito_tech_internship.feature.charts.ui.model

import io.github.shevavarya.avito_tech_internship.core.model.domain.Track

sealed interface ChartsState {
    data object Loading : ChartsState
    data object EmptyError : ChartsState
    data object ServerError : ChartsState
    data object NetworkError : ChartsState
    data class Content(val tracks: List<Track>) : ChartsState
}