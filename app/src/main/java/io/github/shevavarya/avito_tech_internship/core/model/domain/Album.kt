package io.github.shevavarya.avito_tech_internship.core.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Int,
    val title: String?,
    val cover: String?,
    val coverMedium: String?
): Parcelable
