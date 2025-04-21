package io.github.shevavarya.avito_tech_internship.feature.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import io.github.shevavarya.avito_tech_internship.MiniPlayApplication

class SettingsViewModel(
    private val application: Context,
) : ViewModel() {

    fun isChecked(): Boolean {
        return (application as MiniPlayApplication).darkTheme
    }

    fun switchTheme(isChecked: Boolean) {
        (application as MiniPlayApplication).switchTheme(isChecked)
    }
}