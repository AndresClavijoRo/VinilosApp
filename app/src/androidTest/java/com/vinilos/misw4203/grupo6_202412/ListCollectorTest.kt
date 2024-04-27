package com.vinilos.misw4203.grupo6_202412

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vinilos.misw4203.grupo6_202412.fake_data.FakeDataCollectors
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.view.screens.home.CollectorScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ListCollectorTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val mockRepository = Mockito.mock(VinilosRepository::class.java)
    private val collectorViewModel = CollectorViewModel(vinilosRepository = mockRepository)

    fun initWithCollectors(collectors: List<CollectorDto>, onClick: (String) -> Unit = {}) {
        collectorViewModel.collectorsState.value = collectors
        composeTestRule.setContent {
            CollectorScreen(
                onClick = onClick,
                collectorViewModel = collectorViewModel,
            )
        }
    }

    fun assertCollectorsDisplayed(collectors: List<CollectorDto>) {

        //        Test collectorListItem list items size
        val collectorListItems = composeTestRule.onAllNodes(hasTestTag("collectorListItem")).fetchSemanticsNodes()
        assert(collectorListItems.size == collectors.size)

          //        Test collectorListItem list items content
        for (collector in collectors) {
            collector.name?.let { composeTestRule.onNodeWithText(it).assertExists() }
            collector.email?.let { composeTestRule.onNodeWithText(it).assertExists() }

        }

    }

    @Test
    fun listmultipleCollectorsTest() {
        val collectors = FakeDataCollectors.collectors
        this.initWithCollectors(collectors)
        // Shows Collectors title
        composeTestRule.onNodeWithText("Coleccionistas").assertExists()
        // Check if the collectors are displayed
        this.assertCollectorsDisplayed(collectors)
    }

    @Test
    fun listOneCollectorTest() {
        val collectors = listOf(FakeDataCollectors.collectors[0])
        this.initWithCollectors(collectors)
        // Check one collector is displayed
        this.assertCollectorsDisplayed(collectors)
    }

    @Test
    fun listNoCollectorsTest() {
        val collectors = emptyList<CollectorDto>()
        this.initWithCollectors(collectors)
        // Check no collectors are displayed
        this.assertCollectorsDisplayed(collectors)
        composeTestRule.onNodeWithText("No hay coleccionistas").assertExists()
    }

    @Test
    fun listErrorTest() {
        this.initWithCollectors(emptyList<CollectorDto>())
        val expectedText = "Hubo un error al cargar los coleccionistas"
        collectorViewModel.errorText.value = expectedText
        composeTestRule.onNodeWithText(expectedText).assertExists()
    }

    @Test
    fun listLoadingTest() {
        this.initWithCollectors(emptyList<CollectorDto>())
        collectorViewModel.isLoading.value = true
        composeTestRule.onNodeWithTag("pullRefreshIndicator").assertExists()
    }

    @Test
    fun clickCollectorTest() {
        var collectorId: String? = null;
        val onClick = { id: String -> collectorId = id }
        val collectors = FakeDataCollectors.collectors
        this.initWithCollectors(collectors, onClick)
        for (collector in collectors) {
            composeTestRule.onNodeWithText(collector.name!!).performClick()
            assert(collectorId == collector.id.toString())
        }
    }

}