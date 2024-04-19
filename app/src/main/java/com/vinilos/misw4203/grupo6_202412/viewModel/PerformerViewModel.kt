package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
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

class PerformerViewModel(private val performerRepository: VinilosRepository): ViewModel() {
    private val _artistsState = mutableStateOf<List<ArtistDto>>(emptyList())
    val artistsState: State<List<ArtistDto>> = _artistsState

    init {
        getAllArtists()
    }
    private fun getAllArtists() {
        viewModelScope.launch {
            try {
                val response = performerRepository.getArtist(onResponse = {
                        performes ->  _artistsState.value = performes
                },
                    onFailure = {

                    })
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