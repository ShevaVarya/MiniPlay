package io.github.shevavarya.avito_tech_internship.feature.player.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.core.utils.collectWithLifecycle
import io.github.shevavarya.avito_tech_internship.core.utils.dpToPx
import io.github.shevavarya.avito_tech_internship.core.utils.msToMinute
import io.github.shevavarya.avito_tech_internship.databinding.FragmentPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {

    private val viewModel by viewModel<PlayerViewModel>() {
        parametersOf(args)
    }

    private val args by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARGS, PlayerArgs::class.java)!!
        } else {
            requireArguments().getParcelable(ARGS)!!
        }
    }

    private var isUserSeeking = false

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        setupSeekBar()

        with(viewBinding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            ibPlay.setOnClickListener {
                viewModel.togglePlayback()
            }

            ibPreviousTrack.setOnClickListener {
                viewModel.previousTrack()
            }

            ibFollowTrack.setOnClickListener {
                viewModel.nextTrack()
            }
        }

    }

    override fun observeData() {
        viewModel.duration.collectWithLifecycle(this) {
            viewBinding.seekBar.max = it.toInt()

        }

        viewModel.playbackPosition.collectWithLifecycle(this) {
            if (!isUserSeeking) {
                viewBinding.seekBar.progress = it.toInt()
            }
        }

        viewModel.uiState.collectWithLifecycle(this) {
            applyState(it)
        }

        viewModel.isPlaying.collectWithLifecycle(this) {
            if (it) {
                viewBinding.ibPlay.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.ic_pause
                    )
                )
            } else {
                viewBinding.ibPlay.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(), R.drawable.ic_play_arrow
                    )
                )
            }
        }

    }

    private fun applyState(state: TrackUiState?) {
        state?.let {
            with(viewBinding) {
                trackName.text = state.current.title
                trackArtist.text = state.current.artist.name
                trackTime.text = state.current.duration
                collectionName.text = state.current.album.title
            }

            setImage(state.current.album.coverMedium, viewBinding.trackImage)

            if (state.previous != null) {
                setImage(state.previous.album.cover, viewBinding.prevImage)
                viewBinding.prevImage.isVisible = true
            } else {
                viewBinding.prevImage.isGone = true
            }

            if (state.next != null) {
                setImage(state.next.album.cover, viewBinding.nextImage)
                viewBinding.nextImage.isVisible = true

            } else {
                viewBinding.nextImage.isGone = true
            }
        }
    }

    private fun setImage(uri: String, view: ImageView) {
        Glide.with(this)
            .load(uri)
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .placeholder(R.drawable.placeholder)
            .into(view)
    }

    private fun setupSeekBar() {
        viewBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    viewModel.seekTo(it.toLong())
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.recordTime.text = msToMinute(progress.toLong())
            }
        })
    }

    companion object {
        private const val ARGS = "args"
    }
}