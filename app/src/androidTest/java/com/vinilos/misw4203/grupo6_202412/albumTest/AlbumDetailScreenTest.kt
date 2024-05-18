package com.vinilos.misw4203.grupo6_202412.albumTest

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.AlbumScreenDetail
import com.vinilos.misw4203.grupo6_202412.view.utils.formatDateString
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumDetailUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumDetailViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AlbumDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepository = Mockito.mock(VinilosRepository::class.java)
    private var albumDetailViewModel =
        AlbumDetailViewModel(albumDetailRepository = mockRepository, idDetail = 1)

    @Before
    fun setUp() {
        Thread.sleep(3000)
    }

    @Test
    fun albumDetailScreenSuccesTest() {
        val album = FakeDataAlbums.albums[0]
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)
        composeTestRule.setContent {
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")

        composeTestRule.onNodeWithText(album.name).assertExists()
    }

    @Test
    fun albumInfoCompleteTest() {
        val album = FakeDataAlbums.albums[0]
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)

        composeTestRule.setContent {
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }
        val date = ' '+ formatDateString(
            album.releaseDate!!,
            inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            outputFormat = "dd/MM/yyyy"
        )
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")

        composeTestRule.onNodeWithText(date).assertExists()
        composeTestRule.onNodeWithText(album.genre!!).assertExists()
        composeTestRule.onNodeWithText(album.recordLabel!!).assertExists()
        composeTestRule.onNodeWithTag("albumDescription").performClick()
    }

    @Test
    fun albumArtistTest(){
        val album = FakeDataAlbums.albums[0]
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)

        composeTestRule.setContent {
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")
        album.performers.forEach {
            composeTestRule.onNodeWithText(it.name!!).assertExists()
        }
    }

    @Test
    fun albumTracksTest(){
        val album = FakeDataAlbums.albums[0]
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)

        composeTestRule.setContent {
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")
        album.tracks.forEach { track ->
            composeTestRule.onNodeWithText("${track.name} - ${track.duration}" ).assertExists()
        }
    }

    @Test
    fun albumCommentTest(){
        val album = FakeDataAlbums.albums[0]
        var expectedContentDescription = ""
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)

        composeTestRule.setContent {
            expectedContentDescription = stringResource(R.string.rating_comment_description, album.comments[0].rating!!, 5)
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")
        album.comments.forEach { comment ->
            composeTestRule.onNodeWithText(comment.description!!).assertExists()
        }

        composeTestRule.onNodeWithContentDescription(expectedContentDescription).assertExists()
    }

    @Test
    fun albumDetailScreenErrorTest() {
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Error
        var textError = ""
        var textButtonRefresh = ""
        composeTestRule.setContent {
            textError = stringResource(R.string.error_al_cargar)
            textButtonRefresh = stringResource(R.string.retry)

            AlbumScreenDetail(
                idDetail = "1",
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreen")

        composeTestRule.onNodeWithText(textError).assertExists()
        composeTestRule.onNode(hasText(textButtonRefresh)).assertIsDisplayed()
    }
}