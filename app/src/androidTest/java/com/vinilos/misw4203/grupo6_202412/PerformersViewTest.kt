package com.vinilos.misw4203.grupo6_202412

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.vinilos.misw4203.grupo6_202412.view.navigation.RootNavGraph
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PerformersViewTest  {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun performerTestIfListIsNotEmpty() {
        composeTestRule.setContent {
            RootNavGraph()
        }

        composeTestRule.onNodeWithText("Coleccionista").performClick()
        composeTestRule.onNodeWithText("Artistas").performClick()
        val children = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        assertTrue("Performer list has more than one child", children > 0)
    }

    @Test
    fun performerHasValidImage() {
        composeTestRule.setContent {
            RootNavGraph()
        }

        composeTestRule.onNodeWithText("Coleccionista").performClick()
        composeTestRule.onNodeWithText("Artistas").performClick()
        composeTestRule.onNodeWithTag("artistTag").onChildren()[0].onChildren()[0].assertContentDescriptionEquals("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ruben_Blades_by_Gage_Skidmore.jpg/800px-Ruben_Blades_by_Gage_Skidmore.jpg")
    }

    @Test
    fun screenHasScroll() {
        composeTestRule.setContent {
            RootNavGraph()
        }

        composeTestRule.onNodeWithText("Coleccionista").performClick()
        composeTestRule.onNodeWithText("Artistas").performClick()
        val children1 = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        println("Cantidad Primera ->: ${children1}")
        composeTestRule.onNodeWithTag("artistTag").performScrollToIndex(8)
        val children2 = composeTestRule.onNodeWithTag("artistTag").onChildren().fetchSemanticsNodes().size
        println("Cantidad Segunda ->: ${children2}")
    }
}