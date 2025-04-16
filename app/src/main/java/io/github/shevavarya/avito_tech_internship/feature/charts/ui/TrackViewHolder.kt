package io.github.shevavarya.avito_tech_internship.feature.charts.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.utils.dpToPx
import io.github.shevavarya.avito_tech_internship.databinding.ItemTrackViewBinding

class TrackViewHolder(
    private val binding: ItemTrackViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {

            trackName.text = track.title
            trackArtist.text = track.artist.name
            trackTime.text = track.duration

            Glide.with(binding.root)
                .load(track.album.cover)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f, binding.root.context)))
                .placeholder(R.drawable.placeholder)
                .into(trackImage)
        }
    }
}