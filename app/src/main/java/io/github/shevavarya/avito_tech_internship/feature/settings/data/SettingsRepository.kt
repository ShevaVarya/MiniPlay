package io.github.shevavarya.avito_tech_internship.feature.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.shevavarya.avito_tech_internship.core.utils.Const.SHARED_PREFERENCES_KEY_THEME

interface SettingsRepository {
    fun isNightTheme(): Boolean
    fun saveTheme(isNightTheme: Boolean)
}

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    private var darkTheme = false


    override fun isNightTheme(): Boolean {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_KEY_THEME, darkTheme)
    }

    override fun saveTheme(isNightTheme: Boolean) {
        darkTheme = isNightTheme
        sharedPreferences.edit { putBoolean(SHARED_PREFERENCES_KEY_THEME, darkTheme) }
    }
}