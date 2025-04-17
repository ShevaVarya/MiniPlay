package io.github.shevavarya.avito_tech_internship.core.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val id: Int,
    val name: String,
    val picture: String,
): Parcelable
