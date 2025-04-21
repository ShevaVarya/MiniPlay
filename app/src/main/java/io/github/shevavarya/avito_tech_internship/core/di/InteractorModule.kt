package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.feature.search.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.search.domain.ChartsInteractorImpl
import io.github.shevavarya.avito_tech_internship.feature.settings.domain.SettingsInteractor
import io.github.shevavarya.avito_tech_internship.feature.settings.domain.SettingsInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::ChartsInteractorImpl) bind ChartsInteractor::class
    singleOf(::SettingsInteractorImpl) bind SettingsInteractor::class
}