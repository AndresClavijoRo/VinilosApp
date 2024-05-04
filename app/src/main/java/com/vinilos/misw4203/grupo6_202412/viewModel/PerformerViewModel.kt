package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class PerformerViewModel(private val performerRepository: VinilosRepository): ViewModel() {
    val _performersState = mutableStateOf<List<ArtistDto>>(emptyList())
    val performersState: State<List<ArtistDto>> = _performersState
    var performerDetailState by mutableStateOf<ArtistDto?>(null)

    init {
        getAllPerformers()
    }
    private fun getAllPerformers() {
        viewModelScope.launch {
            try {
                val response = performerRepository.getPerformers(
                    onResponse = {
                        performersList ->  _performersState.value = performersList
                                 },
                    onFailure = {
                        Log.i("Error","Error consumiendo servicio ")
                    })
            } catch (e: Exception) {
                Log.i("Error","Error consumiendo servicio " + e.message)
            }
        }
    }

    fun getPerformerDetail(musicianId: Int) {
        viewModelScope.launch {
            try {
                performerRepository.getPerformerById(
                    onResponse = {
                            performerDetail ->  performerDetailState = performerDetail
                    },
                    onFailure = {
                        Log.i("Error","Error consumiendo servicio ")
                    },
                    musicianId)
            } catch (e: Exception) {
                Log.i("Error","Error consumiendo servicio " + e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                val vinilosRepository = application.vinilosRepository
                PerformerViewModel(performerRepository = vinilosRepository)
            }
        }
    }
}