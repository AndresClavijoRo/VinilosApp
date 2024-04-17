package com.vinilos.misw4203.grupo6_202412

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinilos.misw4203.grupo6_202412.models.dto.MusicianDto
import com.vinilos.misw4203.grupo6_202412.models.service.IMusicianEndpoint
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private lateinit var server: MockWebServer
    private lateinit var api: IMusicianEndpoint
    private lateinit var gson: Gson

    @Before
    fun beforeEach() {
        server = MockWebServer()
        api = VinilosService(server.url("/").toString()).getMusicianEndpoint()
        gson = GsonBuilder().create()
    }

    @After
    fun afterEach() {
        server.shutdown()
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getMusicianList(){
        val dto: ArrayList<MusicianDto> = arrayListOf(MusicianDto())
        val json = gson.toJson(dto)
        server.enqueue(MockResponse().setBody(json))
        val response = api.getMusicianList().execute()
        assertEquals(dto, response.body())
    }



}