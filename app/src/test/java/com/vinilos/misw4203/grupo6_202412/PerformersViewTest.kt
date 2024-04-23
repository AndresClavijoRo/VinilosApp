package com.vinilos.misw4203.grupo6_202412

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class PerformersViewTest  {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testComposableDisplayed() {
        composeTestRule.onNodeWithText("algo").assertExists()
    }
}