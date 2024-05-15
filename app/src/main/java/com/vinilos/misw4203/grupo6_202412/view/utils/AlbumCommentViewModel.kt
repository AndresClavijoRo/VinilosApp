package com.vinilos.misw4203.grupo6_202412.view.utils

import androidx.compose.runtime.mutableStateOf
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto

class AlbumCommentViewModel {
    val isCommentSaved = mutableStateOf(false)
    val comment =  mutableStateOf("")
    val isInvalidComment = mutableStateOf(false)
    val collector = mutableStateOf<CollectorDto?>(null)
    val isInvalidCollector = mutableStateOf(false)
    val rating = mutableStateOf(0)


    fun isInvalidComment() {
        isInvalidComment.value = !(comment.value.any())
    }

    fun isInvalidCollector() {
        isInvalidCollector.value = collector.value == null
    }


    private fun checkAllInputs(): Boolean {
        isInvalidComment()
        isInvalidCollector()

        return !(isInvalidComment.value && isInvalidCollector.value)
    }

    fun saveComment(onComplete: () -> Unit) {
        if (checkAllInputs()) {
            isCommentSaved.value = false
            return
        }

    }

}