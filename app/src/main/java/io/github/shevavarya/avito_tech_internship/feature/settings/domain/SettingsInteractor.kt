package io.github.shevavarya.avito_tech_internship.feature.settings.domain

import io.github.shevavarya.avito_tech_internship.feature.settings.data.SettingsRepository

interface SettingsInteractor {
    fun isNightTheme(): Boolean
    fun saveTheme(isNightTheme: Boolean)
}

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {

    override fun isNightTheme(): Boolean {
        return settingsRepository.isNightTheme()
    }

    override fun saveTheme(isNightTheme: Boolean) {
        settingsRepository.saveTheme(isNightTheme)
    }
}