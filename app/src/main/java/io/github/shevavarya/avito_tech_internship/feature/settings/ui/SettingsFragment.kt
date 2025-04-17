package io.github.shevavarya.avito_tech_internship.feature.settings.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.databinding.FragmentSettingsBinding

class SettingsFragment: BaseFragment<FragmentSettingsBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        //TODO("Not yet implemented")
    }

    override fun observeData() {
        //TODO("Not yet implemented")
    }
}