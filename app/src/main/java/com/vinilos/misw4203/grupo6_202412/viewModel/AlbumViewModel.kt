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
import kotlinx.coroutines.launch


sealed interface AlbumUiState {
    data class Success(val albums: List<AlbumDto>) : AlbumUiState
    object Error : AlbumUiState
    object Loading : AlbumUiState
}

class AlbumViewModel(
    private val albumRepository: VinilosRepository
): ViewModel(){

    var albumUiState: AlbumUiState by mutableStateOf(AlbumUiState.Loading)
        private set

    private var dataLoaded = false

    init {
        getAllAlbums()
    }
    private fun getAllAlbums() {
        if (dataLoaded) {
            return
        }

        viewModelScope.launch {
            try {
               albumRepository.getAlbums(
                    onResponse = {
                            albumList -> albumUiState = AlbumUiState.Success(albumList)
                    },
                    onFailure = {
                        Log.i("Error","Error consumiendo servicio ")
                        albumUiState = AlbumUiState.Error
                    })
            } catch (e: Exception) {
                Log.i("Error","Error consumiendo servicio " + e.message)
                albumUiState = AlbumUiState.Error
            }
        }
    }

    fun refreshAlbums() {
        albumUiState = AlbumUiState.Loading
        dataLoaded = false
        getAllAlbums()
    }

    companion object {
        private var instance: AlbumViewModel? = null

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                val vinilosRepository = application.vinilosRepository
                AlbumViewModel(albumRepository = vinilosRepository)
                instance ?: AlbumViewModel(albumRepository = vinilosRepository).also { instance = it }
            }
        }
    }
}