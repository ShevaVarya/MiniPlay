package io.github.shevavarya.avito_tech_internship.core.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AudioPlayerManager(
    private val context: Context
) {

    var trackChangedCallback: ((Int) -> Unit)? = null

    private val exoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                mediaItem?.mediaId?.toLongOrNull()?.let { index ->
                    trackChangedCallback?.invoke(index.toInt())
                }
            }
        })
    }

    val player: ExoPlayer get() = exoPlayer

    private val handler = Handler(Looper.getMainLooper())

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val progressRunnable = object : Runnable {
        override fun run() {
            _playbackPosition.value = exoPlayer.currentPosition
            _duration.value = exoPlayer.duration
            handler.postDelayed(this, UPDATE_INTERVAL)
        }
    }

    @OptIn(UnstableApi::class)
    fun startPlayerService() {
        val intent = Intent(context, PlayerService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    @OptIn(UnstableApi::class)
    fun stopPlayerService() {
        val intent = Intent(context, PlayerService::class.java)
        context.stopService(intent)
    }

    fun prepareMediaItem(url: Uri) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun preparePlaylist(tracks: List<Track>, startIndex: Int = 0) {
        val items = tracks.mapIndexed { index, track ->
            MediaItem.Builder()
                .setUri(track.preview)
                .setMediaId(index.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist.name)
                        .build()
                )
                .build()
        }
        exoPlayer.setMediaItems(items, startIndex, C.TIME_UNSET)
        exoPlayer.prepare()
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    private fun play() {
        exoPlayer.play()
        startPlayerService()
        handler.post(progressRunnable)
    }

    private fun pause() {
        exoPlayer.pause()
        handler.removeCallbacks(progressRunnable)
    }

    fun playNext() {
        exoPlayer.seekToNext()
    }

    fun playPrevious() {
        exoPlayer.seekToPrevious()
    }

    fun release() {
        exoPlayer.release()
        stopPlayerService()
        handler.removeCallbacks(progressRunnable)
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    companion object {
        private const val UPDATE_INTERVAL = 200L
    }

}