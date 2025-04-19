package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import io.github.shevavarya.avito_tech_internship.core.component.MediaStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val componentModule = module {

    single { AudioPlayerManager(androidContext()) }
    single { MediaStoreManager(androidContext()) }

}