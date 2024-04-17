package com.vinilos.misw4203.grupo6_202412

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.service.IArtistEndpoint
import com.vinilos.misw4203.grupo6_202412.models.service.ICollectorEndpoint
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class VinilosServiceUnitTest {

    private lateinit var server: MockWebServer
    private lateinit var gson: Gson
    private lateinit var artistApi: IArtistEndpoint
    private lateinit var collectorApi: ICollectorEndpoint

    @Before
    fun beforeEach() {
        server = MockWebServer()
        gson = GsonBuilder().create()
        artistApi = VinilosService(server.url("/").toString()).getArtistEndpoint()
        collectorApi = VinilosService(server.url("/").toString()).getCollectorEndpoint()
    }

    @After
    fun afterEach() {
        server.shutdown()
    }

    @Test
    fun test_GetArtistList(){
        val dto: ArrayList<ArtistDto> = arrayListOf(ArtistDto())
        val json = gson.toJson(dto)
        server.enqueue(MockResponse().setBody(json))
        val response = artistApi.getArtistList().execute()
        assertEquals(dto, response.body())
    }

    @Test
    fun test_GetCollectorList(){
        val dto: ArrayList<CollectorDto> = arrayListOf(CollectorDto())
        val json = gson.toJson(dto)
        server.enqueue(MockResponse().setBody(json))
        val response = collectorApi.getCollectorsList().execute()
        assertEquals(dto, response.body())
    }

}