package io.github.shevavarya.avito_tech_internship.core.component

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.Album
import io.github.shevavarya.avito_tech_internship.core.model.domain.Artist
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.utils.msToMinute

interface MediaStoreManager {
    fun getLocalTracks(): List<Track>
}

class MediaStoreManagerImpl(private val context: Context): MediaStoreManager {

    override fun getLocalTracks(): List<Track> {
        val tracks = mutableListOf<Track>()
        val contentResolver = context.contentResolver

        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: context.getString(R.string.media_store_unknown_track)
                val artist = cursor.getString(artistColumn) ?: context.getString(R.string.media_store_unknown_artist)
                val duration = cursor.getLong(durationColumn)
                val uri = ContentUris.withAppendedId(collection, id)
                val artwork = getEmbeddedArtworkAsBase64(uri)

                tracks.add(createTrack(id, title, artist, duration, uri, artwork))
            }
        }

        return tracks
    }

    private fun getEmbeddedArtworkAsBase64(filePath: Uri): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, filePath)
            val art = retriever.embeddedPicture
            retriever.release()

            if (art != null) {
                // Преобразуем в Base64 строку
                val base64 = Base64.encodeToString(art, Base64.DEFAULT)
                "data:image/jpeg;base64,$base64"

            } else {
                null
            }
        } catch (e: Exception) {
            retriever.release()
            null
        }
    }

    private fun createTrack(
        id: Long,
        title: String,
        artist: String,
        duration: Long,
        uri: Uri,
        artwork: String?
    ): Track {
        return Track(
            id = id,
            title = title,
            album = Album(
                id = 0,
                title = null,
                cover = artwork,
                coverMedium = artwork
            ),
            artist = Artist(
                id = 0,
                name = artist,
                picture = null
            ),
            duration = msToMinute(duration),
            preview = uri
        )
    }
}
