package io.github.shevavarya.avito_tech_internship

import android.app.Application
import io.github.shevavarya.avito_tech_internship.core.di.componentModule
import io.github.shevavarya.avito_tech_internship.core.di.dataModule
import io.github.shevavarya.avito_tech_internship.core.di.interactorModule
import io.github.shevavarya.avito_tech_internship.core.di.repositoryModule
import io.github.shevavarya.avito_tech_internship.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MiniPlayApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiniPlayApplication)
            modules(listOf(dataModule, repositoryModule, interactorModule, viewModelModule, componentModule))
        }
    }
}