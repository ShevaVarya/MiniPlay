package io.github.shevavarya.avito_tech_internship.core.model

data class TrackDto(
    val id: Long,
    val title: String,
    val album: AlbumDto,
    val artist: ArtistDto,
    val duration: Int,
    val preview: String,
)