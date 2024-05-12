package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed interface AlbumUiState {
    data class Success(val albums: List<AlbumDto>) : AlbumUiState
    data object Error : AlbumUiState
    data object Loading : AlbumUiState
}

open class AlbumViewModel(
    private val albumRepository: VinilosRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var albumUiState: AlbumUiState by mutableStateOf(AlbumUiState.Loading)

    init {
        getAllAlbums()
    }

    fun getAllAlbums() {
        albumUiState = AlbumUiState.Loading
        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    albumRepository.getAlbums(
                        onResponse = { albumList ->
                            albumUiState = AlbumUiState.Success(albumList)
                        },
                        onFailure = {
                            Log.i("Error", "Error consumiendo servicio ")
                            albumUiState = AlbumUiState.Error
                        })
                } catch (e: Exception) {
                    Log.i("Error", "Error consumiendo servicio " + e.message)
                    albumUiState = AlbumUiState.Error
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                val vinilosRepository = application.vinilosRepository
                AlbumViewModel(albumRepository = vinilosRepository)
            }
        }
    }
}