package com.vinilos.misw4203.grupo6_202412.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumRequest
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CreateAlbumViewModel(
    private val albumRepository: VinilosRepository,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val coverRegex = """https://(www.)?([^\r\n\t\f\v ><;,]+).(jpeg|jpg|gif|png)$""".toRegex(
        setOf(
            RegexOption.MULTILINE,
            RegexOption.IGNORE_CASE
        )
    )

    val name = mutableStateOf("")
    val isInvalidName = mutableStateOf(false)

    val cover = mutableStateOf("")
    val isInvalidCover = mutableStateOf(false)

    val releaseDate = mutableStateOf(LocalDate.now())

    val genre = mutableStateOf("")
    val isInvalidGenre = mutableStateOf(false)

    val recordLabel = mutableStateOf("")
    val isInvalidRecordLabel = mutableStateOf(false)

    val description = mutableStateOf("")
    val isInvalidDescription = mutableStateOf(false)

    val isNewAlbumSaved = mutableStateOf(false)

    fun isInvalidAlbumName() {
        isInvalidName.value = !(name.value.any())
    }

    fun isInvalidAlbumCover() {
        isInvalidCover.value = !(cover.value.any() && coverRegex.matches(cover.value))
    }

    fun isInvalidAlbumGenre() {
        isInvalidGenre.value = !(genre.value.any())
    }

    fun isInvalidAlbumRecordLabel() {
        isInvalidRecordLabel.value = !(recordLabel.value.any())
    }

    fun isInvalidAlbumDescription() {
        isInvalidDescription.value = !(description.value.any())
    }

    fun showAlbumNameSupportText(): String {
        return when {
            !isInvalidName.value -> "Max 100 characters"
            !name.value.any() -> "El nombre no debe estar vacio"
            else -> ""
        }
    }

    fun showAlbumCoverSupportText(): String {
        return when {
            !isInvalidCover.value -> "Max 100 characters"
            !cover.value.any() -> "El cover no debe estar vacio"
            !coverRegex.matches(cover.value) -> "El cover debe ser una URL"
            else -> ""
        }
    }

    fun showAlbumDropdownSupportText(): String {
        return when {
            isInvalidGenre.value -> "Debe seleccionar un elemento"
            else -> ""
        }
    }

    fun showAlbumDescriptionSupportText(): String {
        return when {
            !isInvalidDescription.value -> "Max 500 characters"
            !description.value.any() -> "La descripcion no debe estar vacia"
            else -> ""
        }
    }

    fun saveAlbum(onComplete: () -> Unit) {
        if (!checkAllInputs()) {
            isNewAlbumSaved.value = false
            return
        }

        viewModelScope.launch(dispatcherIO) {
            try {
                albumRepository.createAlbums(buildRequest())
                isNewAlbumSaved.value = true
            } catch (e: Exception) {
                Log.i("Error", "Error consumiendo servicio " + e.message)
                isNewAlbumSaved.value = false
            }
            withContext(dispatcherMain) {
                onComplete()
            }
        }
    }

    private fun checkAllInputs(): Boolean {
        isInvalidAlbumName()
        isInvalidAlbumCover()
        isInvalidAlbumGenre()
        isInvalidAlbumRecordLabel()
        isInvalidAlbumDescription()

        return !(isInvalidName.value && isInvalidCover.value && isInvalidGenre.value
                && isInvalidRecordLabel.value && isInvalidDescription.value)
    }

    private fun buildRequest(): AlbumRequest {
        return AlbumRequest(
            name.value,
            cover.value,
            releaseDate.value.toString(),
            genre.value,
            recordLabel.value,
            description.value
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                CreateAlbumViewModel(albumRepository = application.vinilosRepository)
            }
        }
    }
}

enum class Genre(val value: String) {
    CLASSICAL("Classical"),
    SALSA("Salsa"),
    ROCK("Rock"),
    FOLK("Folk")
}

enum class RecordLabel(val value: String) {
    SONY_MUSIC("Sony Music"),
    EMI("EMI"),
    DISCOS_FUENTES("Discos Fuentes"),
    ELEKTRA("Elektra"),
    FANIA_RECORDS("Fania Records")
}