package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.feature.search.data.ChartsRepository
import io.github.shevavarya.avito_tech_internship.feature.search.data.ChartsRepositoryImpl
import io.github.shevavarya.avito_tech_internship.feature.settings.data.SettingsRepository
import io.github.shevavarya.avito_tech_internship.feature.settings.data.SettingsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::ChartsRepositoryImpl) bind ChartsRepository::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
}