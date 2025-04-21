package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManagerImpl
import io.github.shevavarya.avito_tech_internship.core.component.MediaStoreManager
import io.github.shevavarya.avito_tech_internship.core.component.MediaStoreManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val componentModule = module {

    single<AudioPlayerManager> { AudioPlayerManagerImpl(androidContext()) }
    single<MediaStoreManager> { MediaStoreManagerImpl(androidContext()) }
}