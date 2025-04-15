package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.feature.charts.data.ChartsRepository
import io.github.shevavarya.avito_tech_internship.feature.charts.data.ChartsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::ChartsRepositoryImpl) bind ChartsRepository::class
}