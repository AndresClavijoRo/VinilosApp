@file:OptIn(ExperimentalMaterial3Api::class)
package com.vinilos.misw4203.grupo6_202412.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel

@Composable
fun CommentAlbumForm(
    idDetail: String,
    onClickBack: () -> Unit,
    collectorViewModel: CollectorViewModel = viewModel(factory = CollectorViewModel.Factory),
) {
    val collectors = collectorViewModel.collectorsState.value
    val options: List<CollectorDto> = collectors

    val (isValidCollector, setIsValidCollector) = remember { mutableStateOf(false) }
    val (collectorSelected: CollectorDto?, setCollectorSelected) = remember {
        mutableStateOf<CollectorDto?>(
            null
        )
    }
    val (rating, setRating) = remember { mutableIntStateOf(1) }
    val (comment, setComment) = remember { mutableStateOf("") }
    val (isValidComment, setIsValidComment) = remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopBarComment(
                onClickBack = onClickBack,
                save = {
                    Log.d("CommentAlbum", "Save")
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
                CollectorSelect(
                    options = options,
                    isValidCollector = isValidCollector,
                    setIsValidCollector = setIsValidCollector,
                    collectorSelected = collectorSelected,
                    setCollectorSelected = setCollectorSelected,
                )

                StarRatingSelect(
                    maxStars = 5,
                    rating = rating,
                    setRating = setRating,
                )

                CommentAlbumForm(
                    comment = comment,
                    setComment = setComment,
                    isValidComment = isValidComment,
                    setIsValidComment = setIsValidComment
                )
            }
        }
    }
}


@Composable
fun CollectorSelect(
    options: List<CollectorDto>,
    isValidCollector: Boolean,
    setIsValidCollector: (Boolean) -> Unit,
    collectorSelected: CollectorDto? = null,
    setCollectorSelected: (CollectorDto?) -> Unit,
) {

    var textState by remember { mutableStateOf(TextFieldValue(text = "")) }
    var filteredOptions: List<CollectorDto> by remember { mutableStateOf(options) }
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && filteredOptions.isNotEmpty()
    setIsValidCollector(collectorSelected == null && textState.text.isNotEmpty() && !expanded)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded,
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = textState,
            onValueChange = { newValue ->
                setCollectorSelected(null)
                textState = newValue.copy(
                    text = newValue.text,
                    selection = TextRange(newValue.text.length)
                )
                filteredOptions = options.filter { fruit ->
                    fruit.name!!.trim().contains(textState.text.trim(), ignoreCase = true)
                }.take(10)
                setExpanded(true)
            },
            singleLine = true,
            label = { Text(stringResource(R.string.collector)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            isError = isValidCollector,
            supportingText = {
                if (isValidCollector) Text(stringResource(R.string.selectionValidator))
            },
            keyboardActions = KeyboardActions(onDone = {
                if (collectorSelected == null) {
                    val firstCollector = filteredOptions.firstOrNull()
                    setCollectorSelected(
                        firstCollector
                    )
                    textState = TextFieldValue(
                        firstCollector?.name ?: "",
                        TextRange(firstCollector?.name?.length ?: 0)
                    )
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
                        textState = TextFieldValue(option.name!!, TextRange(option.name!!.length))
                        setCollectorSelected(option)
                        setExpanded(false)
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
    rating: Int,
    setRating: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = Icons.Rounded.Star
            val iconTintColor = if (isSelected) StarEnable else StarDisable
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            setRating(i)
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun CommentAlbumForm(
    comment: String,
    setComment: (String) -> Unit,
    isValidComment: Boolean,
    setIsValidComment: (Boolean) -> Unit
) {

    val textError = remember { mutableStateOf("") }
    val characterCount = remember { mutableIntStateOf(comment.length) }

    OutlinedTextField(
        value = comment,
        onValueChange = {
            setComment(it)
            textError.value = messageError(it)
            setIsValidComment(textError.value.isEmpty())
            characterCount.intValue = it.length
        },
        label = { Text(stringResource(R.string.comment)) },
        maxLines = 5,
        minLines = 5,
        supportingText = {
            if (isValidComment) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingrese un comentario")
                    Text("${characterCount.intValue}/300")
                }
            } else {
                Text(textError.value)
            }
        },
        isError = !isValidComment,
        modifier = Modifier.fillMaxWidth(),
    )
}

fun messageError(comment: String): String {
    if (comment.isEmpty()) {
        return "Ingrese un comentario"
    }
    if (comment.length > 300) {
        return "El comentario no puede superar los 300 caracteres"
    }
    return ""
}

@Composable
fun TopBarComment(onClickBack: () -> Unit, save: () -> Unit) {
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
            TextButton(onClick = save) {
                Text(text = "Save")
            }
        }
    )
}