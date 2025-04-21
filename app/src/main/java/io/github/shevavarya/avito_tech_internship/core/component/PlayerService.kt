package io.github.shevavarya.avito_tech_internship.core.component

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import io.github.shevavarya.avito_tech_internship.MainActivity
import io.github.shevavarya.avito_tech_internship.R
import org.koin.android.ext.android.inject

/**
 * PlayerService - класс_ который служит для создания ForegroundService для отображания и управления PlayerNotifaction
 */

@UnstableApi
class PlayerService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private val audioPlayerManager: AudioPlayerManager by inject()

    private var notificationManager: PlayerNotificationManager? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, audioPlayerManager.player).build()

        val mediaDescriptionAdapter = createMediaDescriptionAdapter()

        notificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID
        )
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(createNotificationListener())
            .setChannelNameResourceId(R.string.notification_channel_music)
            .setChannelDescriptionResourceId(R.string.notification_channel_music_description)
            .build()

        notificationManager?.setPlayer(audioPlayerManager.player)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return null
    }

     // TODO(Добавить изображение к плееру)
    @OptIn(UnstableApi::class)
    private fun createMediaDescriptionAdapter(): PlayerNotificationManager.MediaDescriptionAdapter {
        return object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.currentMediaItem?.mediaMetadata?.title
                    ?: getString(R.string.player_notification_unknown_track)
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(this@PlayerService, MainActivity::class.java)
                return PendingIntent.getActivity(
                    this@PlayerService, 0, intent, PendingIntent.FLAG_IMMUTABLE
                )
            }

            override fun getCurrentContentText(player: Player): CharSequence {
                return player.currentMediaItem?.mediaMetadata?.artist
                    ?: getString(R.string.player_notification_unknown_artist)
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return null
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun createNotificationListener(): PlayerNotificationManager.NotificationListener {
        return object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                stopSelf()
            }

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                if (ongoing) {
                    startForeground(notificationId, notification)
                } else {
                    stopForeground(false)
                }
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onDestroy() {
        notificationManager?.setPlayer(null)
        audioPlayerManager.release()
        mediaSession = null
        notificationManager = null
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "audio_playback_channel"
    }
}
