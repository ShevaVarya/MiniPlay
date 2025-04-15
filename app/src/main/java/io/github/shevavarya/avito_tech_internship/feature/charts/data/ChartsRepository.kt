package io.github.shevavarya.avito_tech_internship.feature.charts.data

import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.model.toDomain
import io.github.shevavarya.avito_tech_internship.core.network.NetworkClient

interface ChartsRepository {
    suspend fun searchTracks(query: String): Result<List<Track>>

    suspend fun getTrackChart(): Result<List<Track>>
}

class ChartsRepositoryImpl(
    private val networkClient: NetworkClient
) : ChartsRepository {

    override suspend fun searchTracks(query: String): Result<List<Track>> {
        return networkClient.searchTracks(query).map { it.toDomain() }
    }

    override suspend fun getTrackChart(): Result<List<Track>> {
        return networkClient.getTrackChart().map { it.toDomain() }
    }

}