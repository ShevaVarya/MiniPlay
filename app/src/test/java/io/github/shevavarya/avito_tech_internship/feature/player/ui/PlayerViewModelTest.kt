package io.github.shevavarya.avito_tech_internship.feature.player.ui

import io.github.shevavarya.avito_tech_internship.core.component.AudioPlayerManager
import io.github.shevavarya.avito_tech_internship.core.model.domain.Album
import io.github.shevavarya.avito_tech_internship.core.model.domain.Artist
import io.github.shevavarya.avito_tech_internship.core.model.domain.PlayerArgs
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
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

class PlayerViewModelTest : KoinTest {

    private val audioPlayerManager: AudioPlayerManager = mock()

    private lateinit var args: PlayerArgs

    private val playerViewModel: PlayerViewModel by inject()

    @Before
    fun setUp() {
        val trackList = createTracks()

        args = PlayerArgs(
            tracks = trackList,
            trackId = 1L
        )

        startKoin {
            modules(
                module {
                    single { audioPlayerManager }
                    single { args }
                    single { PlayerViewModel(get(), get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun togglePlayback() {
        playerViewModel.togglePlayback()
        verify(audioPlayerManager, times(1)).togglePlayPause()
    }

    @Test
    fun nextTrack() {
        playerViewModel.nextTrack()
        verify(audioPlayerManager, times(1)).playNext()
    }

    @Test
    fun previousTrack() {
        playerViewModel.previousTrack()
        verify(audioPlayerManager, times(1)).playPrevious()
    }

    @Test
    fun seekTo() {
        playerViewModel.seekTo(10L)
        verify(audioPlayerManager, times(1)).seekTo(10L)
    }

    private fun createTracks(): List<Track> {
        return listOf(
            Track(
                id = 1,
                title = "name1",
                album = Album(1, null, null, null),
                artist = Artist(1, "artist1", null),
                duration = "00:00",
                preview = mock()
            ),
            Track(
                id = 2,
                title = "name2",
                album = Album(1, null, null, null),
                artist = Artist(1, "artist2", null),
                duration = "00:00",
                preview = mock()
            )
        )
    }
}