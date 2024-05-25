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

class PerformerViewModel(
    private val performerRepository: VinilosRepository,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val _performersState = mutableStateOf<List<ArtistDto>>(emptyList())
    val performersState = _performersState
    var isLoading: Boolean = true
    var isError: Boolean = false

    init {
        getAllPerformers()
    }
    fun getAllPerformers() {
        viewModelScope.launch {
            withContext(dispatcherIO) {
                try {
                    val performers = performerRepository.getPerformers()
                    withContext(dispatcherMain) {
                        _performersState.value = performers
                    }
                    isLoading = false
                } catch (e: Exception) {
                    isError = true
                    isLoading = false
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
                PerformerViewModel(performerRepository = application.vinilosRepository)
            }
        }
    }
}