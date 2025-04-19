package io.github.shevavarya.avito_tech_internship.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.databinding.ItemTrackViewBinding

class TrackAdapter(
    private val onClick: (Track) -> Unit = {},

    ) : ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onClick(track)
        }
    }
}

