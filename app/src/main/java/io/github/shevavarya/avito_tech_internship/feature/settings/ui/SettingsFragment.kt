package io.github.shevavarya.avito_tech_internship.feature.settings.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import io.github.shevavarya.avito_tech_internship.R
import io.github.shevavarya.avito_tech_internship.core.ui.BaseFragment
import io.github.shevavarya.avito_tech_internship.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            switchNightMode.isChecked = viewModel.isChecked()
            switchNightMode.setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }

            writeInDev.setOnClickListener {
                sendEmail(
                    getString(R.string.settings_intent_mail),
                    getString(R.string.settings_intent_message),
                    getString(R.string.settings_intent_subject)
                )
            }
        }
    }

    override fun observeData() {}

    private fun sendEmail(mail: String, message: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ContextCompat.startActivity(requireContext(), intent, null)
    }
}