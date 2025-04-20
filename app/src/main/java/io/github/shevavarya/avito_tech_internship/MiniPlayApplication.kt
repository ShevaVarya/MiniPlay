package io.github.shevavarya.avito_tech_internship

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import io.github.shevavarya.avito_tech_internship.core.di.componentModule
import io.github.shevavarya.avito_tech_internship.core.di.dataModule
import io.github.shevavarya.avito_tech_internship.core.di.interactorModule
import io.github.shevavarya.avito_tech_internship.core.di.repositoryModule
import io.github.shevavarya.avito_tech_internship.core.di.viewModelModule
import io.github.shevavarya.avito_tech_internship.feature.settings.domain.SettingsInteractor
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MiniPlayApplication() : Application() {

    var darkTheme = false
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiniPlayApplication)
            modules(
                listOf(
                    dataModule,
                    repositoryModule,
                    interactorModule,
                    viewModelModule,
                    componentModule
                )
            )
        }
        darkTheme = settingsInteractor.isNightTheme()
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsInteractor.saveTheme(darkThemeEnabled)
    }

    override fun onTerminate() {
        super.onTerminate()
        get<AudioPlayerManager>().release()
    }
}