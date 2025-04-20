package io.github.shevavarya.avito_tech_internship.feature.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.utils.dpToPx
import io.github.shevavarya.avito_tech_internship.databinding.ItemCoverBinding

class CoverViewHolder(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.album.coverMedium)
            .transform(RoundedCorners(dpToPx(8f, itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(binding.coverImage)
    }
}