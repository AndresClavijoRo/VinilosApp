package com.vinilos.misw4203.grupo6_202412

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class VinilosServiceUnitTest {

    private val gson: Gson = GsonBuilder().create()
    private val server: MockWebServer = MockWebServer()
    private lateinit var vinilosServiceAdapter: VinilosService

    @Before
    fun setUp() {
        vinilosServiceAdapter = VinilosService(server.url("/").toString())
    }

    @After
    fun teardown() = runTest{
        server.shutdown()
    }

    @Test
    fun test_GetAlbumList()= runTest {
        val dto: ArrayList<AlbumDto> = arrayListOf(AlbumDto(id = 1, name = "Album 1"))
        val json = gson.toJson(dto)

        server.enqueue(MockResponse().setBody(json))
        val response: ArrayList<AlbumDto> = getAlbumsSuspend()
        assertEquals(dto, response)
    }

    @Test
    fun test_GetArtistList() = runTest {
        val dto: ArrayList<ArtistDto> = arrayListOf(ArtistDto())
        val json = gson.toJson(dto)

        server.enqueue(MockResponse().setBody(json))
        val response: ArrayList<ArtistDto> = getPerformersSuspend()
        assertEquals(dto, response)
    }

    @Test
    fun test_GetCollectorList() = runTest {
        val dto: ArrayList<CollectorDto> = arrayListOf(CollectorDto())
        val json = gson.toJson(dto)

        server.enqueue(MockResponse().setBody(json))
        val response: ArrayList<CollectorDto> = getCollectorsSuspend()
        assertEquals(dto, response)
    }

    @Test
    fun test_CreateAlbum() = runTest {
        val request = AlbumDto(id = 1, name = "Album 1")
        val json = gson.toJson(request)

        server.enqueue(MockResponse().setBody(json))
        val response: AlbumDto = createAlbumSuspend(request)
        assertEquals(request, response)
    }

    private suspend fun getAlbumsSuspend(): ArrayList<AlbumDto> = suspendCoroutine { continuation ->
        vinilosServiceAdapter.getAlbums({
            continuation.resume(it)
        }, {
            continuation.resumeWithException(RuntimeException("Error occurred: $it"))
        })
    }
    private suspend fun getPerformersSuspend(): ArrayList<ArtistDto> = suspendCoroutine { continuation ->
        vinilosServiceAdapter.getPerformers({
            continuation.resume(it)
        }, {
            continuation.resumeWithException(RuntimeException("Error occurred: $it"))
        })
    }
    private suspend fun getCollectorsSuspend(): ArrayList<CollectorDto> = suspendCoroutine { continuation ->
        vinilosServiceAdapter.getCollectors({
            continuation.resume(it)
        }, {
            continuation.resumeWithException(RuntimeException("Error occurred: $it"))
        })
    }

    private suspend fun createAlbumSuspend(request: AlbumDto): AlbumDto =
        suspendCoroutine { continuation ->
            vinilosServiceAdapter.createAlbums(request, {
                continuation.resume(it)
            }, {
                continuation.resumeWithException(RuntimeException("Error occurred: $it"))
            })
        }
}