package io.github.shevavarya.avito_tech_internship

import android.app.Application
import io.github.shevavarya.avito_tech_internship.core.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MiniPlayApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiniPlayApplication)
            modules(listOf(dataModule))
        }
    }
}