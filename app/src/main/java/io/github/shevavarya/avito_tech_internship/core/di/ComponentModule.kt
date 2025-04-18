package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val componentModule = module {

    single { AudioPlayerManager(androidContext()) }
}