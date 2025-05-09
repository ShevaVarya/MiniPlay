package io.github.shevavarya.avito_tech_internship.feature.player.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.core.utils.collectWithLifecycle
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

    private val coverAdapter by lazy {
        CoverAdapter(args.tracks)
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                requireContext(),
                getString(R.string.player_notifications_forbidden_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        setupSeekBar()
        initAdapter()

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

    override fun onStart() {
        super.onStart()
        checkAndRequestNotificationPermission()
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
                if (state.current.album.title != null) {
                    collectionName.text = state.current.album.title
                } else {
                    collectionName.isGone = true
                    textCollectionName.isGone = true
                }

                coverRecyclerView.smoothScrollToPosition(coverAdapter.getScrollPosition(state.current.id))
            }
        }
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        with(viewBinding) {

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            coverRecyclerView.layoutManager = layoutManager

            coverRecyclerView.adapter = coverAdapter
            coverRecyclerView.setOnTouchListener { _, _ -> true }

            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(coverRecyclerView)

            coverRecyclerView.post {
                val initialPosition = args.tracks.indexOfFirst { it.id == args.trackId }
                coverRecyclerView.scrollToPosition(initialPosition)
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    companion object {
        private const val ARGS = "args"
    }
}