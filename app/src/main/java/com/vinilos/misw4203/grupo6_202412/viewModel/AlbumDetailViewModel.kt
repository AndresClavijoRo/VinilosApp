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


sealed interface AlbumDetailUiState {
    data class Success(val album: AlbumDto) : AlbumDetailUiState
    data object Error : AlbumDetailUiState
    data object Loading : AlbumDetailUiState
}

open class AlbumDetailViewModel(
    private val albumDetailRepository: VinilosRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var albumDetailUiState: AlbumDetailUiState by mutableStateOf(AlbumDetailUiState.Loading)

    fun getAllAlbumById(id: Int) {
        albumDetailUiState = AlbumDetailUiState.Loading
        viewModelScope.launch {
            withContext(dispatcher){
                try {
                    albumDetailRepository.getAlbumById(
                        id = id,
                        onResponse = { album ->
                            albumDetailUiState = AlbumDetailUiState.Success(album)
                        },
                        onFailure = {
                            Log.i("Error", "Error consumiendo servicio ")
                            albumDetailUiState = AlbumDetailUiState.Error
                        })
                } catch (e: Exception) {
                    Log.i("Error", "Error consumiendo servicio " + e.message)
                    albumDetailUiState = AlbumDetailUiState.Error
                }
            }
        }
    }

    companion object {
        val Factory: (Int) -> ViewModelProvider.Factory = {
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                    val vinilosRepository = application.vinilosRepository
                    AlbumDetailViewModel(
                        albumDetailRepository = vinilosRepository
                    )
                }
            }
        }
    }
}