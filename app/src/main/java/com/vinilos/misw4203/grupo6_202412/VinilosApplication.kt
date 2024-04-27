package com.vinilos.misw4203.grupo6_202412

import android.app.Application
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import com.vinilos.misw4203.grupo6_202412.models.service.VinilosService

class VinilosApplication: Application() {

    private val BASE_URL_API_SERVICE: String = "https://vinyls-grupo-6-back-1a983feb9f59.herokuapp.com/"

    val vinilosService by lazy { VinilosService.getInstance(BASE_URL_API_SERVICE) }
    val vinilosRepository by lazy { VinilosRepository.create(vinilosService) }
}