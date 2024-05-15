package com.vinilos.misw4203.grupo6_202412.viewModel

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.VinilosApplication
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumCommentRequest
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.models.repository.VinilosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumCommentViewModel(
    private val albumCommentRepository: VinilosRepository,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    val isCommentSaved = mutableStateOf(false)
    val comment = mutableStateOf("")
    val isInvalidComment = mutableStateOf(false)
    val collector = mutableStateOf<CollectorDto?>(null)
    val textStateCollector = mutableStateOf(TextFieldValue(text = ""))
    val isInvalidCollector = mutableStateOf(false)
    val rating = mutableIntStateOf(1)
    val isLoading = mutableStateOf(false)

    fun isInvalidCollector() {
        isInvalidCollector.value = collector.value == null ||  (collector.value == null && textStateCollector.value.text.isNotEmpty())
    }

    fun isInvalidComment() {
        isInvalidComment.value = comment.value.isEmpty() || comment.value.length > 300
    }

    private fun checkValidAllInputs(): Boolean {
        isInvalidComment()
        isInvalidCollector()

        return (isInvalidComment.value || isInvalidCollector.value)
    }

    fun showCollectorSupportText(context: Context):String{
        return when {
            collector.value == null && textStateCollector.value.text.isNotEmpty() -> context.getString(
                R.string.selectionValidator)
            else -> ""
        }
    }

    fun showCommentSupportText(context: Context):String{
            return when {
            comment.value.isEmpty() -> context.getString(R.string.inputCommentValidator)
            comment.value.length > 300 -> context.getString(R.string.commentLengthValidator)
            else -> context.getString(R.string.inputCommentValidator)
        }
    }

    fun saveComment(albumId: Int, onComplete: () -> Unit) {
        if (checkValidAllInputs()) {
            isCommentSaved.value = false
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            withContext(dispatcherIO) {
                try {
                    albumCommentRepository.createAlbumComment(albumId, buildRequest())
                    isCommentSaved.value = true
                } catch (e: Exception) {
                    isCommentSaved.value = false
                }
                withContext(dispatcherMain) {
                    onComplete()
                    isLoading.value = false
                }
            }
        }

    }

    private fun buildRequest(): AlbumCommentRequest {
        return AlbumCommentRequest(
            description = comment.value,
            collector = collector.value!!,
            rating = rating.intValue
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VinilosApplication)
                val vinilosRepository = application.vinilosRepository
                AlbumCommentViewModel(albumCommentRepository = vinilosRepository)
            }
        }
    }

}