package com.vinilos.misw4203.grupo6_202412.performer_test

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText

import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.PerformerDetailScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerDetailViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class PerformersDetailViewTest {
    private lateinit var viewModel: PerformerDetailViewModel
    private val repository: VinilosRepository = Mockito.mock()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        viewModel = PerformerDetailViewModel(repository)
    }

    @Test
    fun displayPerformersDetailScreen() {
        var performer =  ArtistDto(1,"test1","https://acortar.link/T7Mc10");
        viewModel.performerDetailState = performer
        composeTestRule.setContent {
            PerformerDetailScreen(performerId = performer.id.toString(), onClick = { }, viewModel)
        }
        composeTestRule.onNodeWithTag("PerformerDetailTitle").assertExists()
    }

    @Test
    fun detailIfAlbumsIsNotEmpty() {
        var albums = arrayListOf(
            AlbumDto(1,"Album1","https://acortar.link/T7Mc10",),
            AlbumDto(2,"Album2","https://acortar.link/T7Mc10"))
        var performer =  ArtistDto(1,"test1","https://acortar.link/T7Mc10");
        performer.albums = albums
        viewModel.performerDetailState = performer
        composeTestRule.setContent {
            PerformerDetailScreen(performerId = performer.id.toString(), onClick = { }, viewModel)
        }

        val albumsList = composeTestRule.onNodeWithTag("albumChildTag").onChildren().fetchSemanticsNodes().size
        Assert.assertTrue("Performer has more than one Album", albumsList > 1)
    }

    @Test
    fun detailIfAlbumsIstEmpty() {
        var performer =  ArtistDto(1,"DetailEmpty","https://acortar.link/T7Mc10");
        performer.albums = arrayListOf()

        lateinit var notFoundMessage: String
        viewModel.performerDetailState = performer
        composeTestRule.setContent {
            notFoundMessage = stringResource(R.string.no_albums_found)
            PerformerDetailScreen(performerId = performer.id.toString(), onClick = { }, viewModel)
        }

        composeTestRule.onNodeWithText(notFoundMessage).assertExists()
    }

    @Test
    fun noBirthDatePresent() {
        var performer =  ArtistDto(1,"DetailEmpty","https://acortar.link/T7Mc10",
            description = "This is the description test");
        performer.albums = arrayListOf()
        viewModel.performerDetailState = performer
        composeTestRule.setContent {
            PerformerDetailScreen(performerId = performer.id.toString(), onClick = { }, viewModel)
        }

        composeTestRule.onNodeWithTag("birthDateTag").assertTextContains("No Data",true, true)

    }

    @Test
    fun detailIfHasDescription() {
        var performer =  ArtistDto(1,"DetailEmpty","https://acortar.link/T7Mc10");
        performer.albums = arrayListOf()
        viewModel.performerDetailState = performer
        composeTestRule.setContent {
            PerformerDetailScreen(performerId = performer.id.toString(), onClick = { }, viewModel)
        }

        composeTestRule.onNodeWithTag("descriptionTag").assertExists()
    }
}