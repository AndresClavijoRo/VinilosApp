package com.vinilos.misw4203.grupo6_202412.performer_test

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.home.PerformerScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class PerformersViewTest  {
    private lateinit var viewModel: PerformerViewModel
    private val repository: VinilosRepository = mock()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        viewModel = PerformerViewModel(repository)
    }

    @Test
    fun displayPerformersView() {
        composeTestRule.setContent {
            PerformerScreen(onClick = { }, viewModel)
        }
        composeTestRule.onNodeWithTag("PerformerTitle").assertExists()
    }
    @Test
    fun performerTestIfListIsNotEmpty() {
        val list = listOf(ArtistDto(1,"test1","https://acortar.link/T7Mc10"),
            ArtistDto(2,"test2","https://acortar.link/T7Mc10"))
        viewModel.performersState.value = list

        composeTestRule.setContent {
            PerformerScreen(onClick = { }, viewModel)
        }
        val children = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        assertTrue("Performer list has more than one child", children > 0)
    }

    @Test
    fun performerTestIfListIsEmpty() {
        viewModel.performersState.value = ArrayList<ArtistDto>()

        composeTestRule.setContent {
            PerformerScreen(onClick = { }, viewModel)
        }
        val children = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        assertTrue("Performer list has no childred", children == 0)
    }

    @Test
    fun performerHasValidImage() {
        val list = listOf(ArtistDto(1,"test1","https://acortar.link/T7Mc10"),
            ArtistDto(2,"test2","https://acortar.link/T7Mc10"))
        viewModel.performersState.value = list
        composeTestRule.setContent {
            PerformerScreen(onClick = { }, viewModel)
        }

        composeTestRule.onNodeWithTag("artistTag").onChildren()[0].
        onChildren()[0].assertExists()
    }

    @Test
    fun screenHasScroll() {
        val myArrayList = ArrayList<ArtistDto>()
        for (i in 1..50) {
            myArrayList.add(ArtistDto(i,"test#${i}","https://acortar.link/T7Mc10"))
        }
        viewModel.performersState.value = myArrayList
        composeTestRule.setContent {
            PerformerScreen(onClick = { }, viewModel)
        }
        val children1 = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        composeTestRule.onNodeWithTag("artistTag").performScrollToIndex(20)
        val children2 = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        assertTrue(children2>children1)
    }
}