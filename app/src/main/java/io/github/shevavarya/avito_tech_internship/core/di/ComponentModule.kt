package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val componentModule = module {

    singleOf(::AudioPlayerManager)
}