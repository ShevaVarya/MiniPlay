package io.github.shevavarya.avito_tech_internship.core.di

import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.feature.search.ui.SearchViewModel
import io.github.shevavarya.avito_tech_internship.feature.player.ui.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (args: PlayerArgs) -> PlayerViewModel(get(), args) }

}