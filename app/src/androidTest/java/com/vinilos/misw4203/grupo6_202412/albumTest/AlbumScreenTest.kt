package com.vinilos.misw4203.grupo6_202412.albumTest

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.home.AlbumScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AlbumScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepository = Mockito.mock(VinilosRepository::class.java)
    private var albumViewModel = AlbumViewModel(albumRepository = mockRepository)

    @Before
    fun setUp() {
        Thread.sleep(3000)
    }

    @Test
    fun albumScreenSuccesTest() {
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        var titleAlbumList = ""
        composeTestRule.setContent {
            titleAlbumList = stringResource(R.string.albumTitle)
            AlbumScreen(
                onClickAlbumsDetail = { },
                onClickCreateAlbum = { },
                albumViewModel = albumViewModel,
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest")
        composeTestRule.onNodeWithText(titleAlbumList).assertExists()
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
    }

    @Test
    fun albumScreenErrorTest() {
        albumViewModel.albumUiState = AlbumUiState.Error

        var textError = ""
        var textButtonRefresh = ""
        composeTestRule.setContent {
            textError = stringResource(R.string.error_al_cargar)
            textButtonRefresh = stringResource(R.string.retry)

            AlbumScreen(
                onClickAlbumsDetail = { },
                onClickCreateAlbum = { },
                albumViewModel =albumViewModel
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest")
        composeTestRule.onNodeWithText(textError).assertExists()
        composeTestRule.onNode(hasText(textButtonRefresh)).assertIsDisplayed()
    }

    @Test
    fun albumScreenCargandoTest() {
        albumViewModel.albumUiState = AlbumUiState.Loading
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                onClickCreateAlbum = { },
                albumViewModel = albumViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest")
        composeTestRule.onNodeWithTag("pullRefreshIndicator").assertExists()
    }

    @Test
    fun albumScreenRefreshTest() {
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                onClickCreateAlbum = { },
                albumViewModel = albumViewModel
            )
        }
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
        albumViewModel.getAllAlbums()
        Thread.sleep(1000)
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
    }

    @Test
    fun albumDetailCallTest() {
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        var albumId = ""
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { albumId = it },
                onClickCreateAlbum = { },
                albumViewModel = albumViewModel
            )
        }
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).performClick()
        assert(albumId == FakeDataAlbums.albums[0].id.toString())
    }

    @Test
    fun albumScreenSuccesNoData() {
        albumViewModel.albumUiState = AlbumUiState.Success(emptyList())
        var textNoData = ""

        composeTestRule.setContent {
            textNoData = stringResource(R.string.no_data)
            AlbumScreen(
                onClickAlbumsDetail = { },
                onClickCreateAlbum = { },
                albumViewModel = albumViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest")
        composeTestRule.onNodeWithText(textNoData).assertExists()
    }
}