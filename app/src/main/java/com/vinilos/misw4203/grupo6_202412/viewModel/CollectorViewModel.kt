package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.launch

class CollectorViewModel(private val performerRepository: VinilosRepository): ViewModel() {
    private val _collectorsState = mutableStateOf<List<CollectorDto>>(emptyList())
    val collectorsState = _collectorsState

    init {
        getAllCollectors()
    }

    private fun getAllCollectors() {
        viewModelScope.launch {
            try {
                val response = performerRepository.getCollectors(
                    onResponse = {
                        collectorsList ->  _collectorsState.value = collectorsList
                    },
                    onFailure = {
                        Log.i("Error","Error consumiendo servicio ")
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
                CollectorViewModel(performerRepository = vinilosRepository)
            }
        }
    }

}