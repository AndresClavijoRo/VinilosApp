package com.vinilos.misw4203.grupo6_202412.albumTest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataAlbums
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.AlbumScreenDetail
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

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("albumDetailScreenSuccesTest")

        composeTestRule.onNodeWithText(album.name).assertExists()
        album.description?.let { composeTestRule.onNodeWithText(it).assertExists() }
    }

    @Test
    fun coverAlbumDetailScreenSuccesTest() {
        val album = FakeDataAlbums.albums[0]
        albumDetailViewModel.albumDetailUiState =
            AlbumDetailUiState.Success(album)
//        var commentBtnString = "";

        composeTestRule.setContent {
//            commentBtnString = stringResource(id = R.string.btn_album_comment)
            AlbumScreenDetail(
                idDetail = album.id.toString(),
                onClickBack = {},
                onClickCommentAlbum = {},
                albumDetailViewModel = albumDetailViewModel,
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true)
            .printToLog("coverAlbumDetailScreenSuccesTest")

        composeTestRule.onNodeWithText(album.name).assertExists()

//        composeTestRule.onNode(
//            matcher = hasClickAction() and
//                    hasText(commentBtnString),
//        ).assertExists()
//
//        composeTestRule.onNode(
//            matcher = hasContentDescription(album.name)
//        ).assertExists().printToLog("coverAlbumDetailScreenSuccesTest")
    }
}