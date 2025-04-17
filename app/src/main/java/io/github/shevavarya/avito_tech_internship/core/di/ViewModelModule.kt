package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.feature.charts.ui.ChartsViewModel
import io.github.shevavarya.avito_tech_internship.feature.player.ui.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ChartsViewModel)
    viewModel { (args: PlayerArgs) -> PlayerViewModel(get(), args) }

}