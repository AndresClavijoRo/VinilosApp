package com.vinilos.misw4203.grupo6_202412

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import com.vinilos.misw4203.grupo6_202412.view.screens.CreateAlbumFormScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.CreateAlbumViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.Genre
import com.vinilos.misw4203.grupo6_202412.viewModel.RecordLabel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class CreateAlbumFormScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val gson: Gson = GsonBuilder().create()
    private val server: MockWebServer = MockWebServer()
    private lateinit var vinilosServiceAdapter: VinilosService
    private lateinit var mockRepository: VinilosRepository
    private lateinit var createAlbumViewModel: CreateAlbumViewModel

    @Before
    fun setUp() {
        vinilosServiceAdapter = VinilosService(server.url("/").toString())
        mockRepository = VinilosRepository(vinilosServiceAdapter)
        createAlbumViewModel = CreateAlbumViewModel(albumRepository = mockRepository)
        Thread.sleep(2000)
    }

    @Test
    fun showAllControlsCreateAlbum() {
        composeTestRule.setContent {
            CreateAlbumFormScreen(
                back = { },
                viewModel = createAlbumViewModel)
        }
        composeTestRule.onNodeWithText("Album Name").assertExists()
        composeTestRule.onNodeWithText("Cover").assertExists()
        composeTestRule.onNodeWithText("Release date").assertExists()
        composeTestRule.onNodeWithText("Genre").assertExists()
        composeTestRule.onNodeWithText("Record Label").assertExists()
        composeTestRule.onNodeWithText("Description").assertExists()
    }

    @Test
    fun avoidCreateAlbumWithEmptyFields(){
        composeTestRule.setContent {
            CreateAlbumFormScreen(
                back = { },
                viewModel = createAlbumViewModel)
        }
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.onNodeWithText("El cover no debe estar vacio").assertExists()
    }

    @Test
    fun createAlbumSuccessfully(){
        inputNewAlbumDummy()
        val dto = AlbumDto(0, createAlbumViewModel.name.value)
        val json = gson.toJson(dto)
        server.enqueue(MockResponse().setBody(json))

        composeTestRule.setContent {
            CreateAlbumFormScreen(
                back = { },
                viewModel = createAlbumViewModel)
        }

        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitUntil { createAlbumViewModel.isNewAlbumSaved.value }
        assert(createAlbumViewModel.isNewAlbumSaved.value)
    }

    private fun inputNewAlbumDummy() {
        createAlbumViewModel.name.value = "Twisted Sister"
        createAlbumViewModel.cover.value =
            "https://fridaymusic.com/cdn/shop/products/twisted-sister-tear-it-loose-cover-3k_1024x.jpg"
        createAlbumViewModel.releaseDate.value = LocalDate.parse("1972-11-23")
        createAlbumViewModel.genre.value = Genre.ROCK.value
        createAlbumViewModel.recordLabel.value = RecordLabel.SONY_MUSIC.value
        createAlbumViewModel.description.value = "Awesome album."
    }

}