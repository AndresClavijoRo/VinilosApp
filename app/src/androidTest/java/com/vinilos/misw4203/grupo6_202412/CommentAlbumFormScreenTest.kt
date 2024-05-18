package com.vinilos.misw4203.grupo6_202412

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataCollectors
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumCommentRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import com.vinilos.misw4203.grupo6_202412.view.screens.CommentAlbumForm
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumCommentViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel
import okhttp3.internal.wait
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentAlbumFormScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val gson: Gson = GsonBuilder().create()
    private val server: MockWebServer = MockWebServer()
    private lateinit var vinilosServiceAdapter: VinilosService
    private lateinit var mockRepository: VinilosRepository
    private lateinit var albumCommentViewModel: AlbumCommentViewModel
    private lateinit var collectorViewModel: CollectorViewModel

    @Before
    fun setUp() {
        vinilosServiceAdapter = VinilosService(server.url("/").toString())
        mockRepository = VinilosRepository(vinilosServiceAdapter)
        albumCommentViewModel = AlbumCommentViewModel( albumCommentRepository = mockRepository)
        collectorViewModel = CollectorViewModel( vinilosRepository = mockRepository)
        collectorViewModel.collectorsState.value = FakeDataCollectors.collectors
        Thread.sleep(3000)
    }

    @Test
    fun showAllControlsCommentAlbum() {
        var textInputCollector = ""
        var textInputComment = ""

        composeTestRule.setContent {
            textInputCollector = stringResource(R.string.collector)
            textInputComment = stringResource(R.string.comment)

            CommentAlbumForm(
                idDetail = "1",
                onClickBack = { },
                collectorViewModel = collectorViewModel,
                commentViewModel = albumCommentViewModel
            )
        }

        composeTestRule.onNodeWithText(textInputCollector).assertExists()
        composeTestRule.onNodeWithText(textInputComment).assertExists()
        composeTestRule.onNodeWithTag("StarRatingSelect").assertExists()

    }

    @Test
    fun avoidCreateCommentWithEmptyFields(){
        var textoValidatorComment = ""
        var textoValidatorCollector = ""
        var btnSaveText = ""
        composeTestRule.setContent {
            textoValidatorComment = stringResource(R.string.inputCommentValidator)
            textoValidatorCollector = stringResource(R.string.selectionValidator)
            btnSaveText = stringResource(R.string.btn_save)

            CommentAlbumForm(
                idDetail = "1",
                onClickBack = { },
                collectorViewModel = collectorViewModel,
                commentViewModel = albumCommentViewModel
            )
        }
        composeTestRule.onNodeWithText(btnSaveText).performClick()
        composeTestRule.onNodeWithText(textoValidatorComment).assertExists()
        composeTestRule.onNodeWithText(textoValidatorCollector).assertExists()
    }

    @Test
    fun commentWithMoreThan300Characters(){
        var textoValidatorComment = ""
        var labelComment = ""
        composeTestRule.setContent {
            textoValidatorComment = stringResource(R.string.commentLengthValidator)
            labelComment = stringResource(R.string.comment)

            CommentAlbumForm(
                idDetail = "1",
                onClickBack = { },
                collectorViewModel = collectorViewModel,
                commentViewModel = albumCommentViewModel
            )
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AlbumScreenTest")
        composeTestRule.onNodeWithText(labelComment).assertExists()
        composeTestRule.onNodeWithText(labelComment).performTextInput("a".repeat(301))
        composeTestRule.onNodeWithText(textoValidatorComment).assertExists()
    }

    @Test
    fun collectorSelected(){
        var labelCollector = ""
        composeTestRule.setContent {
            labelCollector = stringResource(R.string.collector)
            CommentAlbumForm(
                idDetail = "1",
                onClickBack = { },
                collectorViewModel = collectorViewModel,
                commentViewModel = albumCommentViewModel
            )
        }
       val collector =  collectorViewModel.collectorsState.value[0]
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("CommentAlbumForm")
        val collectorNode = composeTestRule.onNodeWithText(labelCollector)
        collectorNode.performTextInput(collector.name!!.substring(0, collector.name!!.length-3))
        Thread.sleep(1000)
        collectorNode.assertExists()
        composeTestRule.onNodeWithText(collector.name!!).performClick().assertExists()
    }
    @Test
    fun commentAlbumSuccessfully(){
        var labelComment = ""
        var labelCollector = ""
        val collector = collectorViewModel.collectorsState.value.random()
        var btnSaveText = ""
        composeTestRule.setContent {
            labelComment = stringResource(R.string.comment)
            labelCollector = stringResource(R.string.collector)
            btnSaveText = stringResource(R.string.btn_save)

            CommentAlbumForm(
                idDetail = "1",
                onClickBack = { },
                collectorViewModel = collectorViewModel,
                commentViewModel = albumCommentViewModel
            )
        }
        val collectorNode = composeTestRule.onNodeWithText(labelCollector)
        collectorNode.performTextInput(collector.name!!.substring(0, collector.name!!.length-3))
        Thread.sleep(500)
        composeTestRule.onNodeWithText(collector.name!!).performClick()

        val ratingStarComments = composeTestRule.onAllNodes(hasTestTag("ratingComment"))
        val ramdomRatingNumber = (0..4).random()
        ratingStarComments[ramdomRatingNumber].performClick()

        val commentNode = composeTestRule.onNodeWithText(labelComment)
        commentNode.performTextInput("This is a comment")

        val dto = AlbumCommentRequest(
            albumCommentViewModel.comment.value,
            albumCommentViewModel.rating.intValue,
            albumCommentViewModel.collector.value!!
        )
        val json = gson.toJson(dto)
        val jsonCollector = gson.toJson(collectorViewModel.collectorsState.value)
        server.enqueue(MockResponse().setBody(jsonCollector))
        server.enqueue(MockResponse().setBody(json))
        composeTestRule.onNodeWithText(btnSaveText).performClick()
        composeTestRule.waitUntil { albumCommentViewModel.isCommentSaved.value }
        assert(albumCommentViewModel.isCommentSaved.value)
    }
}

