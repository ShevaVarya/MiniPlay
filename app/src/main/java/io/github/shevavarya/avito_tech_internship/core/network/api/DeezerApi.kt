package io.github.shevavarya.avito_tech_internship.core.network.api

import io.github.shevavarya.avito_tech_internship.core.model.data.TracksDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApi {

    @GET("/search")
    suspend fun searchTracks(@Query("q") searchQuery: String): TracksDto

    @GET("chart")
    suspend fun getTrackChart(): TracksDto
}