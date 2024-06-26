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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class PerformerDetailViewModel(private val performerRepository: VinilosRepository,
                               private val dispatcherIO: CoroutineDispatcher =
                                   Dispatchers.IO): ViewModel() {
    var performerDetailState = mutableStateOf<ArtistDto?>(null)
    var isLoading: Boolean = true

    fun getPerformerById(musicianId: Int) {
        viewModelScope.launch {
            withContext(dispatcherIO) {
                try {
                    performerRepository.getPerformerById(
                        onResponse = {
                                performerDetail ->  performerDetailState.value = performerDetail
                            isLoading = false
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
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                PerformerDetailViewModel(performerRepository = application.vinilosRepository)
            }
        }
    }
}
