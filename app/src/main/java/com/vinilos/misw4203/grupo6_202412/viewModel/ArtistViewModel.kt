package com.vinilos.misw4203.grupo6_202412.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository

class ArtistViewModel(private val repository: VinilosRepository): ViewModel() {




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as VinilosApplication)
                val vinilosRepository = application.vinilosRepository
                ArtistViewModel(repository = vinilosRepository)
            }
        }
    }
}