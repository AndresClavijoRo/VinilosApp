package com.vinilos.misw4203.grupo6_202412.collectorTest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataCollectors
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.CollectorScreenDetail
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorAlbumsUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorDetailViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

val COLLECTOR_ID=1
class CollectorDetailTest() {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val mockRepository = Mockito.mock(VinilosRepository::class.java)
    private val collectorViewModel = CollectorDetailViewModel(vinilosRepository = mockRepository, collectorId = COLLECTOR_ID)

    @Before
    fun initTestRule(){
        composeTestRule.setContent {
            CollectorScreenDetail(
                idDetail = COLLECTOR_ID.toString(),
                onClickBack = {},
                collectorScreenModel = collectorViewModel
            )
        }
    }
    fun initWithCollector(collector: CollectorDto,albums:List<AlbumDto> = emptyList()) {
        collectorViewModel.collectorUiState = CollectorUiState.Success(collector)
        collectorViewModel.collectoralbumsUiState = CollectorAlbumsUiState.Success(ArrayList(albums))

    }

    fun assertCollectorDisplayed(collector: CollectorDto) {
        collector.email?.let { composeTestRule.onNodeWithText(it).assertExists() }
        collector.telephone?.let { composeTestRule.onNodeWithText(it).assertExists() }
    }
    @Test
    fun baseCollectorDetailTest() {
        for(collector in FakeDataCollectors.collectors){
            this.initWithCollector(collector)
            // Shows Collectors name
            composeTestRule.onNodeWithText(collector.name!!).assertExists()
            // Check if the collectors are displayed
            this.assertCollectorDisplayed(collector)
        }
    }

    @Test
    fun loadingScreenTest() {
        collectorViewModel.collectorUiState = CollectorUiState.Loading
        collectorViewModel.collectoralbumsUiState = CollectorAlbumsUiState.Loading
        // Shows loading
        composeTestRule.onNodeWithTag("pullRefreshIndicator").assertExists()
    }
    @Test
    fun collectorAlbumsTest() {
        // shows albums
        val collector = FakeDataCollectors.collectors[0]
        val albums = FakeDataAlbums.albums.subList(0,2)
        this.initWithCollector(collector,albums)
        for (album in albums) {
            album.name?.let { composeTestRule.onNodeWithText(it).assertExists() }
        }
    }
    @Test
    fun emptyCollectorAlbumsTest() {
        // shows no albums
        val collector = FakeDataCollectors.collectors[0]
        this.initWithCollector(collector)
        composeTestRule.onNodeWithText("El coleccionista no tiene albumes favoritos.").assertExists()
    }
    @Test
    fun loadingCollectorAlbumsTest() {
        collectorViewModel.collectorUiState = CollectorUiState.Success(FakeDataCollectors.collectors[0])
        collectorViewModel.collectoralbumsUiState = CollectorAlbumsUiState.Loading
        // Shows loading
        composeTestRule.onNodeWithTag("loadingAlbums").assertExists()
    }
    @Test
    fun collectorArtistsTest() {
        // Shows musicians as tags
        val collector = FakeDataCollectors.collectors[0]
        val performers = FakeDataAlbums.albums[0].performers
        collector.favoritePerformers = performers
        this.initWithCollector(collector)
        for (performer in performers) {
            performer.name?.let { composeTestRule.onNodeWithText(it).assertExists() }
        }
    }

    @Test
    fun emptyCollectorArtistsTest() {
        // If there are no musicians, show a message
        val collector = FakeDataCollectors.collectors[0]
        collector.favoritePerformers =  ArrayList()
        this.initWithCollector(collector)
        composeTestRule.onNodeWithText("El Coleccionista no tiene artistas favoritos.").assertExists()
    }
    @Test
    fun apiFailureTest() {
        // If there is an API error shows a message
        val error = "Error cargando cosas ajio"
        collectorViewModel.collectorUiState = CollectorUiState.Error(error)
        collectorViewModel.collectoralbumsUiState = CollectorAlbumsUiState.Error(error)
        composeTestRule.onNodeWithText("Error Al cargar").assertExists()

    }
}


