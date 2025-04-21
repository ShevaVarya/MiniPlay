package io.github.shevavarya.avito_tech_internship.feature.search.ui

import io.github.shevavarya.avito_tech_internship.core.component.MediaStoreManager
import io.github.shevavarya.avito_tech_internship.core.model.CustomException
import io.github.shevavarya.avito_tech_internship.core.model.domain.Album
import io.github.shevavarya.avito_tech_internship.core.model.domain.Artist
import io.github.shevavarya.avito_tech_internship.core.model.domain.Track
import io.github.shevavarya.avito_tech_internship.feature.search.domain.ChartsInteractor
import io.github.shevavarya.avito_tech_internship.feature.search.ui.model.Flag
import io.github.shevavarya.avito_tech_internship.feature.search.ui.model.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class SearchViewModelTest : KoinTest {

    private val mockInteractor: ChartsInteractor = mock()
    private val mockMusicManager: MediaStoreManager = mock()
    private val mockArgs: SearchFragmentArgs = mock()
    private val searchViewModel: SearchViewModel by inject()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single { mockInteractor }
                    single { mockMusicManager }
                    single { mockArgs }
                    single {
                        SearchViewModel(get(), get(), get())
                    }
                }
            )
        }

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTrackChart emits content on success`() = runTest {
        //Given
        val track: Track = mock()
        val tracks = listOf(track, track)
        val expectedState = SearchState.Content(tracks, true)
        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockInteractor, times(1)).getTrackChart()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTrackChart handle empty error`() = runTest {
        //Given
        val expectedState = SearchState.EmptyError
        whenever(mockInteractor.getTrackChart()).thenReturn(Result.failure(CustomException.EmptyError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockInteractor, times(1)).getTrackChart()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTrackChart handle request error`() = runTest {
        //Given
        val expectedState = SearchState.EmptyError
        whenever(mockInteractor.getTrackChart()).thenReturn(
            Result.failure(
                CustomException.RequestError(
                    500
                )
            )
        )

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockInteractor, times(1)).getTrackChart()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTrackChart handle network error`() = runTest {
        //Given
        val expectedState = SearchState.NetworkError
        whenever(mockInteractor.getTrackChart()).thenReturn(Result.failure(CustomException.NetworkError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockInteractor, times(1)).getTrackChart()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getTrackChart handle server error`() = runTest {
        //Given
        val expectedState = SearchState.ServerError
        whenever(mockInteractor.getTrackChart()).thenReturn(Result.failure(CustomException.ServerError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockInteractor, times(1)).getTrackChart()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getLocalTrack emits content`() = runTest {
        //Given
        val track: Track = mock()
        val tracks = listOf(track, track)
        val expectedState = SearchState.Content(tracks, true)
        whenever(mockMusicManager.getLocalTracks()).thenReturn(tracks)

        //When
        searchViewModel.getLocalTracks()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockMusicManager, times(1)).getLocalTracks()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getLocalTrack empty error`() = runTest {
        //Given
        val tracks = emptyList<Track>()
        val expectedState = SearchState.EmptyError
        whenever(mockMusicManager.getLocalTracks()).thenReturn(tracks)

        //When
        searchViewModel.getLocalTracks()
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        verify(mockMusicManager, times(1)).getLocalTracks()
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks content when Flag is Local`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.LOCAL)

        val tracks = createTracks()
        whenever(mockMusicManager.getLocalTracks()).thenReturn(tracks)
        searchViewModel.getLocalTracks()
        advanceUntilIdle()

        val filteredTracks = listOf(tracks[0])
        val expectedState = SearchState.Content(filteredTracks, false)

        //When
        searchViewModel.searchTracks("name1")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks empty when Flag is Local`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.LOCAL)

        val tracks = createTracks()
        whenever(mockMusicManager.getLocalTracks()).thenReturn(tracks)
        searchViewModel.getLocalTracks()
        advanceUntilIdle()

        val expectedState = SearchState.EmptyError

        //When
        searchViewModel.searchTracks("name5")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks content when Flag is Network`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.NETWORK)
        val tracks = createTracks()
        val expectedState = SearchState.Content(listOf(tracks[0]), false)

        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))
        whenever(mockInteractor.searchTracks("name1")).thenReturn(Result.success(listOf(tracks[0])))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        searchViewModel.searchTracks("name1")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks empty error when Flag is Network`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.NETWORK)
        val tracks = createTracks()
        val expectedState = SearchState.EmptyError

        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))
        whenever(mockInteractor.searchTracks("name5")).thenReturn(Result.failure(CustomException.EmptyError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        searchViewModel.searchTracks("name5")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks network error when Flag is Network`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.NETWORK)
        val tracks = createTracks()
        val expectedState = SearchState.NetworkError

        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))
        whenever(mockInteractor.searchTracks("name5")).thenReturn(Result.failure(CustomException.NetworkError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        searchViewModel.searchTracks("name5")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks server error when Flag is Network`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.NETWORK)
        val tracks = createTracks()
        val expectedState = SearchState.ServerError

        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))
        whenever(mockInteractor.searchTracks("name5")).thenReturn(Result.failure(CustomException.ServerError))

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        searchViewModel.searchTracks("name5")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchTracks request error when Flag is Network`() = runTest(StandardTestDispatcher()) {
        //Given
        whenever(mockArgs.mode).thenReturn(Flag.NETWORK)
        val tracks = createTracks()
        val expectedState = SearchState.EmptyError

        whenever(mockInteractor.getTrackChart()).thenReturn(Result.success(tracks))
        whenever(mockInteractor.searchTracks("name5")).thenReturn(
            Result.failure(
                CustomException.RequestError(
                    500
                )
            )
        )

        //When
        searchViewModel.getTrackChart()
        advanceUntilIdle()

        searchViewModel.searchTracks("name5")
        advanceUntilIdle()

        //Then
        val state = searchViewModel.state.value
        assertEquals(expectedState, state)
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

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
