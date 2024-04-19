package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vinilos.misw4203.grupo6_202412.models.dto.PerformerDto
import com.vinilos.misw4203.grupo6_202412.models.repository.PerformerRepository
import kotlinx.coroutines.launch

class PerformerViewModel(private val performerRepository: PerformerRepository): ViewModel() {
    val artists: MutableState<List<PerformerDto>> = mutableStateOf(emptyList())
    fun getAllArtists() {
        viewModelScope.launch {
            try {
                val response = performerRepository.getAllPerformers()
                if (response.isNotEmpty()) {
                    artists.value = response
                }
            } catch (e: Exception) {
                Log.i("Error","Error consumiendo servicio " + e.message)
            }
        }
    }
}

class PerformerViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformerViewModel::class.java)) {
            return PerformerViewModel(PerformerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}