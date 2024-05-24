package com.vinilos.misw4203.grupo6_202412.collectorTest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataCollectors
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.CollectorScreenDetail
import com.vinilos.misw4203.grupo6_202412.viewModel.ArtistsListUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorAlbumsUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorDetailViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorUiState
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



private val COLLECTOR = FakeDataCollectors.collectors[0]
private val MUSICIANS = FakeDataCollectors.artists()

class AddFavoriteMusicianTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    private val gson: Gson = GsonBuilder().create()
    private val server: MockWebServer = MockWebServer()
    private lateinit var vinilosServiceAdapter: VinilosService
    private lateinit var mockRepository: VinilosRepository
    private lateinit var collectorViewModel: CollectorDetailViewModel

   val dispatcher: Dispatcher = object : Dispatcher() {

    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {

        return when (request.path) {
            "/collectors/"+ COLLECTOR.id -> {
                MockResponse().setResponseCode(200).setBody(gson.toJson(COLLECTOR))
            }
            "/collectors/"+ COLLECTOR.id +"/albums" -> {
                MockResponse().setResponseCode(200).setBody(gson.toJson(FakeDataAlbums.albums))
            }
            "/musicians" -> {
                MockResponse().setResponseCode(200).setBody(gson.toJson(MUSICIANS))
            }
            else -> {
                MockResponse().setBody("{}")
            }
        }
    }
}

    @Before
    fun initTestRule() {
        server.dispatcher = dispatcher
        vinilosServiceAdapter = VinilosService(server.url("/").toString())
        mockRepository = VinilosRepository(vinilosServiceAdapter)
        collectorViewModel = CollectorDetailViewModel(
            vinilosRepository = mockRepository,
            collectorId = COLLECTOR.id!!
        )
        collectorViewModel.collectoralbumsUiState =
            CollectorAlbumsUiState.Success(ArrayList(FakeDataAlbums.albums))
        composeTestRule.setContent {
            CollectorScreenDetail(
                idDetail = COLLECTOR.id!!.toString(),
                onClickBack = {},
                collectorScreenModel = collectorViewModel
            )
        }
    }

    fun initState(initialFavorites: List<ArtistDto> = emptyList()) {
        COLLECTOR.favoritePerformers = ArrayList(initialFavorites)
        collectorViewModel.collectorUiState = CollectorUiState.Success(COLLECTOR)
        collectorViewModel.showAddFavoriteArtists.value = false
        collectorViewModel.musicianToAdd.value = null

    }

    @Test
    fun addFavoriteMusicianTest() {

        // Mock add favorite artist
        val artistToAdd = MUSICIANS[4]
        // Test Add a favorite musician
        val initialCollectors = FakeDataCollectors.artists().subList(0, 3)
        initState(initialCollectors)
        // Click agregar artista favorito button
        val addFavoriteButton = composeTestRule.onNodeWithText("Agregar artista favorito")
        addFavoriteButton.performClick()
        addFavoriteButton.assertDoesNotExist()
        // Type in the multiselect field
        composeTestRule.onNodeWithText("Artista")
            .performTextInput(artistToAdd.name!!.substring(0, 4))
        // Type in an artist name
        composeTestRule.onNodeWithText(artistToAdd.name!!).performClick()
        composeTestRule.waitUntil { collectorViewModel.musicianToAdd.value == artistToAdd }
        // Click save calls the API and updates the collector with the new favorite artist
        composeTestRule.onNodeWithContentDescription("Confirmar agregar artista como favorito")
            .performClick()
        // The artist is added to the list of favorite artists
        addFavoriteButton.assertExists()
        composeTestRule.onNodeWithText(artistToAdd.name!!).assertExists()

    }

    @Test
    fun loadingAvailableMusiciansTest() {
        initState()
        collectorViewModel.artistsListUiState = ArtistsListUiState.Loading
        composeTestRule.onNodeWithTag("loadingArtists").assertExists()
    }

    @Test
    fun errorLoadingAvailableMusiciansTest() {
        initState()
        val error = "Error cargando artistas disponibles"
        collectorViewModel.artistsListUiState = ArtistsListUiState.Error(error)
        composeTestRule.onNodeWithText(error).assertExists()

    }

    @Test
    fun correctAvailableMusiciansTest() {
        // if an artist is already a favorite, it should not appear in the list
        val initialFavorites = FakeDataCollectors.artists().subList(0, 3)
        val artistToAdd = initialFavorites[0]
        initState(initialFavorites)
        // Should show the artists once
        assert(
            composeTestRule.onAllNodesWithText(artistToAdd.name!!).fetchSemanticsNodes().size == 1
        )

        composeTestRule.onNodeWithText("Agregar artista favorito").performClick()
        // Type in the multiselect field
        composeTestRule.onNodeWithText("Artista")
            .performTextInput(artistToAdd.name!!.substring(0, 4))
        assert(
            composeTestRule.onAllNodesWithText(artistToAdd.name!!).fetchSemanticsNodes().size == 1
        )
    }

}