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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ERROR = "Hubo un error al cargar los coleccionistas"
class CollectorViewModel(
    private val vinilosRepository: VinilosRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main

): ViewModel() {
    private val _collectorsState = mutableStateOf<List<CollectorDto>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorText = mutableStateOf<String?>(null)
    val collectorsState = _collectorsState

    init {
        getAllCollectors()
    }

    fun getAllCollectors() {

        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    val collectorsList = vinilosRepository.getCollectors()
                    withContext(dispatcherMain) {
                        isLoading.value = true
                        errorText.value = null
                        _collectorsState.value = collectorsList
                        isLoading.value = false
                    }
                } catch (e: Exception) {
                    Log.i("Error",ERROR +" " + e.message)
                    errorText.value = ERROR
                    isLoading.value = false
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                CollectorViewModel(vinilosRepository = application.vinilosRepository)
            }
        }
    }

}