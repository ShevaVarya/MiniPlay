package io.github.shevavarya.avito_tech_internship.feature.charts.ui

import androidx.recyclerview.widget.DiffUtil
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track

class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}