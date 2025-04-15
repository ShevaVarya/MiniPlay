package io.github.shevavarya.avito_tech_internship.core.network

import io.github.shevavarya.avito_tech_internship.core.model.CustomException
import io.github.shevavarya.avito_tech_internship.core.model.data.TracksDto
import io.github.shevavarya.avito_tech_internship.core.network.api.DeezerApi
import io.github.shevavarya.avito_tech_internship.core.utils.NetworkChecker
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

interface NetworkClient {
    suspend fun searchTracks(query: String): Result<TracksDto>
    suspend fun getTrackChart(): Result<TracksDto>
}

class NetworkClientImpl(
    private val networkChecker: NetworkChecker,
    private val deezerApi: DeezerApi
) : NetworkClient {

    override suspend fun searchTracks(query: String): Result<TracksDto> {
        return runCatching {
            if (networkChecker.isInternetAvailable()) {
                val tracks = deezerApi.searchTracks(query)

                if (tracks.data.isEmpty()) throw CustomException.EmptyError

                tracks
            } else {
                throw CustomException.NetworkError
            }
        }.recoverCatching { resolveError(it) }
    }

    override suspend fun getTrackChart(): Result<TracksDto> {
        return runCatching {
            if (networkChecker.isInternetAvailable()) {
                val tracks = deezerApi.getTrackChart()

                if (tracks.data.isEmpty()) throw CustomException.EmptyError

                tracks
            } else {
                throw CustomException.NetworkError
            }
        }.recoverCatching {
            resolveError(it)
        }
    }

    private fun resolveError(error: Throwable): Nothing {
        throw when (error) {
            is CustomException.NetworkError -> error
            is CustomException.EmptyError -> error
            is CancellationException -> error
            is IOException -> CustomException.NetworkError
            is HttpException -> {
                if (error.code() in CLIENT_ERROR_RANGE_START..CLIENT_ERROR_RANGE_END) {
                    CustomException.RequestError(error.code())
                } else {
                    CustomException.ServerError
                }
            }

            else -> CustomException.ServerError
        }
    }

    companion object {
        private const val CLIENT_ERROR_RANGE_START = 400
        private const val CLIENT_ERROR_RANGE_END = 499
    }

}