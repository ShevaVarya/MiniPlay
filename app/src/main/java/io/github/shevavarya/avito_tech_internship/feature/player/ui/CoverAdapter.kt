package io.github.shevavarya.avito_tech_internship.feature.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.databinding.ItemCoverBinding

class CoverAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<CoverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverViewHolder {
        val binding = ItemCoverBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoverViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: CoverViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun getScrollPosition(trackId: Long): Int {
        return tracks.indexOfFirst { it.id == trackId }
    }
}