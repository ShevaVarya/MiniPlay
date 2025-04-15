package io.github.shevavarya.avito_tech_internship.core.model.domain

data class Track(
    val id: Long,
    val title: String,
    val album: Album,
    val artist: Artist,
    val duration: String,
    val preview: String,
)