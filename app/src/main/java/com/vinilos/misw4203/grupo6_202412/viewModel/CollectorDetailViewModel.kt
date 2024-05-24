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
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed class CollectorUiState {
    data object Loading : CollectorUiState()
    data class Error(val message: String) : CollectorUiState()
    data class Success(var collector: CollectorDto) :
        CollectorUiState()
}

sealed class CollectorAlbumsUiState {
    data object Loading : CollectorAlbumsUiState()
    data class Error(val message: String) : CollectorAlbumsUiState()
    data class Success(val collectorAlbums: ArrayList<AlbumDto>) :
        CollectorAlbumsUiState()
}

sealed class  ArtistsListUiState {
    data object Loading : ArtistsListUiState()
    data class Error(val message: String) : ArtistsListUiState()
    data class Success(val artists: ArrayList<ArtistDto>) :
        ArtistsListUiState()
}



class CollectorDetailViewModel(
    private val vinilosRepository: VinilosRepository,
    private val collectorId: Int,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val ERROR_MESSAGE = "Error consumiendo servicio "
    private val _showToast = MutableSharedFlow<Boolean>()
    val showToastMessage = _showToast.asSharedFlow()

    var collectorUiState: CollectorUiState by mutableStateOf(CollectorUiState.Loading)
    var collectoralbumsUiState: CollectorAlbumsUiState by mutableStateOf(CollectorAlbumsUiState.Loading)
    var artistsListUiState: ArtistsListUiState by mutableStateOf(ArtistsListUiState.Loading)
    var showAddFavoriteArtists = mutableStateOf(false)
    var musicianToAdd = mutableStateOf<ArtistDto?>(null)
    init {
        fetchData()
    }


    fun fetchData() {
        viewModelScope.launch {
            getCollector()
            getAlbums()
            getArtists()
        }
    }


    private fun getCollector() {
        collectorUiState = CollectorUiState.Loading
        try {
            vinilosRepository.getCollectorbyId(collectorId, {
                collectorUiState = CollectorUiState.Success(it)
            }, {
                collectorUiState = CollectorUiState.Error(it)
            })
        } catch (e: Exception) {
            val message = ERROR_MESSAGE + e.message
            Log.i("Error", message)
            collectorUiState = CollectorUiState.Error(message)
        }
    }

    private fun getAlbums() {
        collectoralbumsUiState = CollectorAlbumsUiState.Loading
        try {
            vinilosRepository.getCollectorAlbums(collectorId, { albumDetails ->
                collectoralbumsUiState =
                    CollectorAlbumsUiState.Success(albumDetails.filter { it.status == "Active" }
                        .map { it.album }
                        .toCollection(ArrayList()))
            }, {
                collectoralbumsUiState = CollectorAlbumsUiState.Error(it)
            })
        } catch (e: Exception) {
            val error = ERROR_MESSAGE + e.message
            Log.i("Error", error)
            collectoralbumsUiState =
                CollectorAlbumsUiState.Error(error)
        }
    }

    private fun getArtists() {
        artistsListUiState = ArtistsListUiState.Loading

        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    val performerList = vinilosRepository.getPerformers()
                    withContext(dispatcherMain) {
                        artistsListUiState = ArtistsListUiState.Success(ArrayList(performerList))
                    }
                } catch (e: Exception) {
                    val error = ERROR_MESSAGE + e.message
                    Log.i("Error", error)
                    artistsListUiState = ArtistsListUiState.Error(error)
                }
            }
        }
    }


    fun toggleShowFavoriteArtist() {
        showAddFavoriteArtists.value = !showAddFavoriteArtists.value
    }
    private fun showAddedSuccessfully() {
        viewModelScope.launch {
            _showToast.emit(true)
        }
    }

    fun addFavoriteArtists() {
        if(musicianToAdd.value == null) return
        val artist = musicianToAdd.value!!
        viewModelScope.launch {
            try {
                vinilosRepository.addFavoriteArtist(collectorId, artist.id!!, {
                    if(collectorUiState is CollectorUiState.Success){
                        val collector = (collectorUiState as CollectorUiState.Success).collector
                        collector.favoritePerformers.add(artist)
                        collectorUiState = CollectorUiState.Loading
                        collectorUiState = CollectorUiState.Success(collector)
                    }
                    showAddedSuccessfully()
                    toggleShowFavoriteArtist()
                    musicianToAdd.value = null
                    Log.i("AddFavoriteArtist", "Success")
                }, {
                    Log.i("AddFavoriteArtist", it)
                })
            } catch (e: Exception) {
                Log.i("AddFavoriteArtist", e.message.toString())
            }
        }
    }

    companion object {
        val Factory: (Int) -> ViewModelProvider.Factory = { idDetail ->
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                    CollectorDetailViewModel(
                        vinilosRepository = application.vinilosRepository, collectorId = idDetail
                    )
                }
            }
        }
    }
}