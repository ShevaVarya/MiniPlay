package io.github.shevavarya.avito_tech_internship.feature.charts.ui

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.bundle.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.core.utils.collectWithLifecycle
import io.github.shevavarya.avito_tech_internship.core.utils.debounce
import io.github.shevavarya.avito_tech_internship.databinding.FragmentChartsBinding
import io.github.shevavarya.avito_tech_internship.feature.charts.ui.model.ChartsState
import io.github.shevavarya.avito_tech_internship.feature.player.ui.PlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class ChartsFragment : BaseFragment<FragmentChartsBinding>() {

    private val viewModel by viewModel<ChartsViewModel>()

    private var trackAdapter: TrackAdapter? = null

    private var onClickDebounce: ((Track) -> Unit?)? = null
    private var onSearchDebounce: ((String) -> Unit)? = null

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChartsBinding {
        return FragmentChartsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        initDebounce()
        initAdapter()

        onTextChanged()
        setListenerEditTextEnd()

        addOnScrollListener()

    }

    private fun initDebounce() {
        onClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startPlayerFragment(track.id)
        }

        onSearchDebounce = debounce(
            SEARCH_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) {
            viewModel.searchTracks(it)
        }
    }

    private fun initAdapter() {
        trackAdapter = TrackAdapter(
            onClick = { vacancy ->
                onClickDebounce?.let { it(vacancy) }
            }
        )

        with(viewBinding) {
            trackList.adapter = trackAdapter
            trackList.itemAnimator = null
            trackList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun onTextChanged() {
        with(viewBinding) {
            searchEditText.doOnTextChanged { text, _, _, _ ->
                val querySearch = text.toString()
                val isEditTextNotEmpty = text.isNullOrEmpty().not()
                switchSearchClearIcon(isEditTextNotEmpty)
                if (viewModel.lastSearchQuery != querySearch) {
                    viewModel.setLoading()
                    onSearchDebounce?.invoke(querySearch)
                }
            }

            searchEditText.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    hideKeyBoard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun switchSearchClearIcon(isEditTextNotEmpty: Boolean) {
        with(viewBinding) {
            val icon = ContextCompat.getDrawable(
                requireContext(),
                if (isEditTextNotEmpty) {
                    R.drawable.ic_clear_24
                } else {
                    R.drawable.ic_search_24
                }
            )
            searchContainer.endIconDrawable = icon
        }
    }

    private fun setListenerEditTextEnd() {
        viewBinding.searchContainer.setEndIconOnClickListener {
            viewBinding.searchEditText.text?.clear()
            hideKeyBoard()
        }
    }

    private fun addOnScrollListener() {
        viewBinding.trackList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    viewBinding.searchEditText.clearFocus()
                    hideKeyBoard()
                }
            }
        })
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity?.currentFocus ?: view
        view?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun observeData() {
        viewModel.state.collectWithLifecycle(this) {
            applyState(it)
        }
    }

    private fun applyState(state: ChartsState) {
        trackAdapter?.submitList(emptyList())
        when (state) {
            is ChartsState.Content -> showContent(state.tracks, state.isChart)
            ChartsState.EmptyError -> showError(getString(R.string.charts_error_empty_list))
            ChartsState.Loading -> showLoading()
            ChartsState.NetworkError -> showError(getString(R.string.charts_error_network))
            ChartsState.ServerError -> showError(getString(R.string.charts_error_server))
        }
    }

    private fun showContent(tracks: List<Track>, isChart: Boolean) {
        trackAdapter?.submitList(tracks)
        with(viewBinding) {
            progressBar.isGone = true
            errorContainer.isGone = true

            trackList.isVisible = true

            toolbar.title = if (isChart)
                getString(R.string.charts_toolbar_title_chart)
            else
                getString(R.string.charts_toolbar_title_search)
        }
    }

    private fun showError(message: String) {
        with(viewBinding) {
            trackList.isGone = true
            progressBar.isGone = true

            errorContainer.isVisible = true
            messageErrorTextView.text = message
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            trackList.isGone = true
            errorContainer.isGone = true

            progressBar.isVisible = true
        }
    }

    private fun startPlayerFragment(id: Long) {
        val state = viewModel.state.value
        if (state is ChartsState.Content) {
            val args = PlayerArgs(state.tracks, id)
            val bundle = Bundle().apply {
                putParcelable("args", args)
            }
            findNavController().navigate(
                R.id.action_chartsFragment_to_playerFragment,
                bundle
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        trackAdapter = null
        onSearchDebounce = null
        onClickDebounce = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val TRACK_ID = "track_id"
    }
}