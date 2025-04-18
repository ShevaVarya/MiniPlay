package io.github.shevavarya.avito_tech_internship.feature.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.databinding.FragmentLibraryBinding

class LibraryFragment: BaseFragment<FragmentLibraryBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        //TODO("Not yet implemented")
    }

    override fun observeData() {
       // TODO("Not yet implemented")
    }
}