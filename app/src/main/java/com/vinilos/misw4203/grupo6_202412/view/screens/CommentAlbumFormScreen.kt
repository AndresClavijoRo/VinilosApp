@file:OptIn(ExperimentalMaterial3Api::class)

package com.vinilos.misw4203.grupo6_202412.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarDisable
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarEnable
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumCommentViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel

@Composable
fun CommentAlbumForm(
    idDetail: String,
    onClickBack: () -> Unit,
    collectorViewModel: CollectorViewModel = viewModel(factory = CollectorViewModel.Factory),
    commentViewModel: AlbumCommentViewModel = viewModel(factory = AlbumCommentViewModel.Factory)
) {
    val collectors = collectorViewModel.collectorsState.value
    val options: List<CollectorDto> = collectors

    Scaffold(
        topBar = {
            TopBarComment(
                isLoading = commentViewModel.isLoading,
                onClickBack = onClickBack,
                save = {
                    commentViewModel.saveComment(idDetail.toInt()) {
                        if (commentViewModel.isCommentSaved.value) onClickBack()
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                CollectorSelect(
                    messageSupport = { commentViewModel.showCollectorSupportText(LocalContext.current) },
                    checkIsValid = { commentViewModel.isInvalidCollector() },
                    options = options,
                    isValidCollector = commentViewModel.isInvalidCollector,
                    collector = commentViewModel.collector,
                    textStateCollector = commentViewModel.textStateCollector
                )

                StarRatingSelect(
                    maxStars = 5,
                    rating = commentViewModel.rating
                )

                CommentAlbumForm(
                    messageSupport = { commentViewModel.showCommentSupportText(LocalContext.current) },
                    checkIsValid = { commentViewModel.isInvalidComment() },
                    comment = commentViewModel.comment,
                    isInvalidComment = commentViewModel.isInvalidComment,
                )
            }
        }
    }
}


@Composable
fun CollectorSelect(
    messageSupport: @Composable () -> String,
    checkIsValid: () -> Unit,
    options: List<CollectorDto>,
    isValidCollector: MutableState<Boolean>,
    collector: MutableState<CollectorDto?>,
    textStateCollector: MutableState<TextFieldValue>,
) {

    var filteredOptions: List<CollectorDto> by remember { mutableStateOf(options) }
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && filteredOptions.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded,
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = textStateCollector.value,
            onValueChange = { newValue ->
                collector.value = null
                textStateCollector.value = newValue.copy(
                    text = newValue.text, selection = TextRange(newValue.text.length)
                )
                filteredOptions = options.filter { collector ->
                    collector.name!!.trim()
                        .contains(textStateCollector.value.text.trim(), ignoreCase = true)
                }.take(5)
                setExpanded(true)
                checkIsValid()
            },
            singleLine = true,
            label = { Text(stringResource(R.string.collector)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            supportingText = {
                Text(messageSupport())
            },
            isError = isValidCollector.value,
            keyboardActions = KeyboardActions(onDone = {
                if (collector.value == null) {
                    val firstCollector = filteredOptions.firstOrNull()
                    collector.value = firstCollector
                    textStateCollector.value = TextFieldValue(
                        firstCollector?.name ?: "",
                        TextRange(firstCollector?.name?.length ?: 0)
                    )
                    checkIsValid()
                }
            }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name!!, style = MaterialTheme.typography.bodyLarge)
                    },
                    onClick = {
                        textStateCollector.value =
                            TextFieldValue(option.name!!, TextRange(option.name!!.length))
                        collector.value = option
                        setExpanded(false)
                        checkIsValid()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun StarRatingSelect(
    maxStars: Int = 5,
    rating: MutableState<Int>
) {
    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating.value
            val icon = Icons.Rounded.Star
            val iconTintColor = if (isSelected) StarEnable else StarDisable
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(selected = isSelected, onClick = {
                        rating.value = i
                    })
                    .width(50.dp)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun CommentAlbumForm(
    messageSupport: @Composable () -> String,
    checkIsValid: () -> Unit,
    comment: MutableState<String>,
    isInvalidComment: MutableState<Boolean>
) {
    OutlinedTextField(
        value = comment.value,
        onValueChange = {
            comment.value = it
            checkIsValid()
        },
        label = { Text(stringResource(R.string.comment)) },
        supportingText = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(messageSupport())
                if (!isInvalidComment.value) {
                    Text("${comment.value.length}/300")
                }
            }
        },
        maxLines = 5,
        minLines = 5,
        isError = isInvalidComment.value,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusEvent { if (it.isFocused) checkIsValid() },
    )
}


@Composable
fun TopBarComment(
    isLoading: MutableState<Boolean>,
    onClickBack: () -> Unit,
    save: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (isLoading.value) {
                Box(modifier = Modifier.padding(end = 16.dp)){
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }

            } else {
                TextButton(onClick = save) {
                    Text(text = "Save")
                }
            }
        }
    )
}