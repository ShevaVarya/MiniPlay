package io.github.shevavarya.avito_tech_internship.core.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerArgs (
    val tracks: List<Track>,
    val trackId: Long
): Parcelable