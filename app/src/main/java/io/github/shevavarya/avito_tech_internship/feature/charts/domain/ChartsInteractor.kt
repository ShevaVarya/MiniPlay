package io.github.shevavarya.avito_tech_internship.feature.charts.domain

import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.feature.charts.data.ChartsRepository

interface ChartsInteractor {
    suspend fun searchTracks(query: String): Result<List<Track>>

    suspend fun getTrackChart(): Result<List<Track>>
}

class ChartsInteractorImpl(private val repository: ChartsRepository) : ChartsInteractor {
    override suspend fun searchTracks(query: String): Result<List<Track>> {
        return repository.searchTracks(query)
    }

    override suspend fun getTrackChart(): Result<List<Track>> {
        return repository.getTrackChart()
    }

}