package io.github.shevavarya.avito_tech_internship.core.model.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val title: String,
    val album: Album,
    val artist: Artist,
    val duration: String,
    val preview: Uri,
) : Parcelable