package io.github.shevavarya.avito_tech_internship.core.model

import io.github.shevavarya.avito_tech_internship.core.model.data.AlbumDto
import io.github.shevavarya.avito_tech_internship.core.model.data.ArtistDto
import io.github.shevavarya.avito_tech_internship.core.model.data.TrackDto
import io.github.shevavarya.avito_tech_internship.core.model.data.TracksDto
import io.github.shevavarya.avito_tech_internship.core.model.domain.Album
import io.github.shevavarya.avito_tech_internship.core.model.domain.Artist
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.utils.msToMinute

fun TracksDto.toDomain(): List<Track> {
    return data.map { it.toDomain() }
}

fun TrackDto.toDomain(): Track {
    return Track(
        id = id,
        title = title,
        album = album.toDomain(),
        artist = artist.toDomain(),
        duration = msToMinute(duration.toLong() * 1000),
        preview = preview
    )
}

fun AlbumDto.toDomain(): Album {
    return Album(
        id = id,
        title = title,
        cover = cover,
        coverMedium = cover_medium
    )
}

fun ArtistDto.toDomain(): Artist {
    return Artist(
        id = id,
        name = name,
        picture = picture
    )
}
 
