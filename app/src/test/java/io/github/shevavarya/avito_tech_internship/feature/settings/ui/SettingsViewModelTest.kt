package io.github.shevavarya.avito_tech_internship.feature.settings.ui

import android.content.Context
import io.github.shevavarya.avito_tech_internship.MiniPlayApplication
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SettingsViewModelTest : KoinTest {

    private val mockApplication: MiniPlayApplication = mock()

    private val settingsViewModel: SettingsViewModel by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<Context> { mockApplication }
                    single { SettingsViewModel(get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `isChecked returns darkTheme value`() {
        // Given
        whenever(mockApplication.darkTheme).thenReturn(true)

        // When
        val result = settingsViewModel.isChecked()

        // Then
        verify(mockApplication, times(1)).darkTheme
        assert(result)
    }

    @Test
    fun `switchTheme calls switchTheme with correct value`() {
        // Given
        val isChecked = true

        // When
        settingsViewModel.switchTheme(isChecked)

        // Then
        verify(mockApplication, times(1)).switchTheme(isChecked)
    }
}
