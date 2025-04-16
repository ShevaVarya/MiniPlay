package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.feature.charts.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.charts.domain.ChartsInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::ChartsInteractorImpl) bind ChartsInteractor::class
}