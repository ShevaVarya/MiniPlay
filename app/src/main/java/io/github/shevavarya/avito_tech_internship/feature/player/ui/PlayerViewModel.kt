package io.github.shevavarya.avito_tech_internship.feature.player.ui

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(
    private val audioPlayerManager: AudioPlayerManager,
    args: PlayerArgs,
) : ViewModel() {

    val playbackPosition: StateFlow<Long> = audioPlayerManager.playbackPosition
    val duration: StateFlow<Long> = audioPlayerManager.duration

    private val _uiState = MutableStateFlow<TrackUiState?>(null)
    val uiState: StateFlow<TrackUiState?> = _uiState

    private val trackList = args.tracks.toMutableList()

    private var currentIndex = trackList.indexOfFirst { it.id == args.trackId }.takeIf { it >= 0 } ?: 0

    val isPlaying: StateFlow<Boolean> = audioPlayerManager.isPlaying

    init {
        _uiState.value = getCurrentUiState()

        audioPlayerManager.preparePlaylist(
            tracks = trackList,
            startIndex = currentIndex
        )

        audioPlayerManager.trackChangedCallback = { index ->
            currentIndex = index
            _uiState.value = getCurrentUiState()
        }

    }

    private fun getCurrentUiState(): TrackUiState {
        val prev = trackList.getOrNull(currentIndex - 1)
        val next = trackList.getOrNull(currentIndex + 1)
        return TrackUiState(
            current = trackList[currentIndex],
            previous = prev,
            next = next
        )
    }

    fun togglePlayback() {
        audioPlayerManager.togglePlayPause()
    }

    fun nextTrack() {
        audioPlayerManager.playNext()
    }

    fun previousTrack() {
        audioPlayerManager.playPrevious()
    }

    fun seekTo(position: Long) {
        audioPlayerManager.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("VARVARA", "onCleared()")
    }

}

data class TrackUiState(
    val current: Track,
    val previous: Track?,
    val next: Track?
)