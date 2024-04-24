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
import com.vinilos.misw4203.grupo6_202412.albumTest.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.home.AlbumScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AlbumScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val mockRepository = Mockito.mock(VinilosRepository::class.java)
    var albumViewModel = AlbumViewModel(albumRepository = mockRepository)

    @Test
    fun albumScreenSuccesTest() {
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                albumViewModel = albumViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest"  )
        composeTestRule.onNodeWithText("Álbumes").assertExists()
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
    }

    @Test
    fun albumScreenErrorTest() {
        Thread.sleep(3000)
        albumViewModel.albumUiState = AlbumUiState.Error

        var textError = ""
        var textButtonRefresh = ""
        composeTestRule.setContent {
            textError = stringResource(R.string.error_al_cargar)
            textButtonRefresh = stringResource(R.string.retry)

            AlbumScreen(
                onClickAlbumsDetail = { },
                albumViewModel
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest"  )
        composeTestRule.onNodeWithText(textError).assertExists()
        composeTestRule.onNode(hasText(textButtonRefresh)).assertIsDisplayed()
    }

    @Test
    fun albumScreenCargandoTest(){
        Thread.sleep(3000)
        albumViewModel.albumUiState = AlbumUiState.Loading
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                albumViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest"  )
        composeTestRule.onNodeWithTag("pullRefreshIndicator").assertExists()
    }

    @Test
    fun albumScreenRefreshTest(){
        Thread.sleep(3000)
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                albumViewModel
            )
        }
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
        albumViewModel.refreshAlbums()
        Thread.sleep(1000)
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
    }

    @Test
    fun albumDetailCallTest(){
        albumViewModel.albumUiState = AlbumUiState.Success(FakeDataAlbums.albums)
        var albumId = ""
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { albumId = it },
                albumViewModel
            )
        }
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).assertExists()
        composeTestRule.onNodeWithText(FakeDataAlbums.albums[0].name).performClick()
        assert(albumId == FakeDataAlbums.albums[0].id.toString());
    }

    @Test
    fun albumScreenSuccesNoData(){
        albumViewModel.albumUiState = AlbumUiState.Success(emptyList())
        composeTestRule.setContent {
            AlbumScreen(
                onClickAlbumsDetail = { },
                albumViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest"  )
        composeTestRule.onNodeWithText("No hay álbumes").assertExists()
    }
}