package io.github.shevavarya.avito_tech_internship.core.component

import android.content.Context
import android.content.Intent
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

/**
 * Класс AudioPlayerManager создается как синглтон и служит для проигрывания музыки с помощью ExoPlayer
 */
interface AudioPlayerManager {

    var trackChangedCallback: ((Int) -> Unit)?
    val player: ExoPlayer
    val isPlaying: StateFlow<Boolean>
    val playbackPosition: StateFlow<Long>
    val duration: StateFlow<Long>

    fun startPlayerService()
    fun stopPlayerService()
    fun preparePlaylist(tracks: List<Track>, startIndex: Int = 0)
    fun togglePlayPause()
    fun playNext()
    fun playPrevious()
    fun release()
    fun seekTo(positionMs: Long)
}

class AudioPlayerManagerImpl(
    private val context: Context
) : AudioPlayerManager {

    override var trackChangedCallback: ((Int) -> Unit)? = null

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

    override val player: ExoPlayer get() = exoPlayer

    private val handler = Handler(Looper.getMainLooper())

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _playbackPosition = MutableStateFlow(0L)
    override val playbackPosition: StateFlow<Long> = _playbackPosition

    private val _duration = MutableStateFlow(0L)
    override val duration: StateFlow<Long> = _duration

    private val progressRunnable = object : Runnable {
        override fun run() {
            _playbackPosition.value = exoPlayer.currentPosition
            _duration.value = exoPlayer.duration
            handler.postDelayed(this, UPDATE_INTERVAL)
        }
    }

    /**
     * Стартовать PlayerService
     */
    @OptIn(UnstableApi::class)
    override fun startPlayerService() {
        val intent = Intent(context, PlayerService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    /**
     * Остановить PlayerService
     */
    @OptIn(UnstableApi::class)
    override fun stopPlayerService() {
        val intent = Intent(context, PlayerService::class.java)
        context.stopService(intent)
    }

    /**
     * Подготовка плейлиста к проигрыванию
     */
    override fun preparePlaylist(tracks: List<Track>, startIndex: Int) {
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

    /**
     * Обработка паузы или плей
     */
    override fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    /**
     * Начать или продолжить проигрывание
     */
    private fun play() {
        exoPlayer.play()
        startPlayerService()
        handler.post(progressRunnable)
    }

    /**
     * Приостановить проигрывание
     */
    private fun pause() {
        exoPlayer.pause()
        handler.removeCallbacks(progressRunnable)
    }

    /**
     * Перейти к следующему треке
     */
    override fun playNext() {
        exoPlayer.seekToNext()
    }

    /**
     * Перейти к предыдущему треку
     */
    override fun playPrevious() {
        exoPlayer.seekToPrevious()
    }

    /**
     * Освободить плеер
     */
    override fun release() {
        exoPlayer.release()
        stopPlayerService()
        handler.removeCallbacks(progressRunnable)
    }

    /**
     * Перейти к позиции
     */
    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    companion object {
        private const val UPDATE_INTERVAL = 200L
    }

}