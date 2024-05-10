@file:OptIn(ExperimentalMaterial3Api::class)

package com.vinilos.misw4203.grupo6_202412.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CreateAlbumViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.Genre
import com.vinilos.misw4203.grupo6_202412.viewModel.RecordLabel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CreateAlbumForm(
    back: () -> Unit,
    viewModel: CreateAlbumViewModel = viewModel(factory = CreateAlbumViewModel.Factory)
) {

    Scaffold(topBar = {
        TopBar(
            back,
            save = { viewModel.saveAlbum()/*; back()*/ }
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                AlbumName(
                    { viewModel.showAlbumNameSupportText() },
                    { viewModel.isInvalidAlbumName() },
                    viewModel.name,
                    viewModel.isInvalidName
                )

                AlbumCoverUrl(
                    { viewModel.showAlbumCoverSupportText() },
                    { viewModel.isInvalidAlbumCover() },
                    viewModel.cover,
                    viewModel.isInvalidCover
                )

                AlbumReleaseDatePicker(viewModel.releaseDate)

                AlbumGenre(
                    { viewModel.showAlbumDropdownSupportText() },
                    { viewModel.isInvalidAlbumGenre() },
                    viewModel.genre,
                    viewModel.isInvalidGenre
                )

                AlbumRecordLabel(
                    { viewModel.showAlbumDropdownSupportText() },
                    { viewModel.isInvalidAlbumRecordLabel() },
                    viewModel.recordLabel,
                    viewModel.isInvalidRecordLabel
                )

                AlbumDescription(
                    { viewModel.showAlbumDescriptionSupportText() },
                    { viewModel.isInvalidAlbumDescription() },
                    viewModel.description,
                    viewModel.isInvalidDescription
                )
            }
        }
    }
}

@Composable
private fun AlbumName(
    messageSupport: () -> String,
    checkIsValid: () -> Unit,
    name: MutableState<String>,
    isInvalidName: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusEvent { if (it.isFocused) checkIsValid() },
        value = name.value,
        isError = isInvalidName.value,
        onValueChange = {
            name.value = manageLength(it, 100)
            checkIsValid()
        },
        label = { Text("Album Name") },
        supportingText = {
            Text(messageSupport())
        }
    )
}

@Composable
private fun AlbumCoverUrl(
    messageSupport: () -> String,
    checkIsValid: () -> Unit,
    cover: MutableState<String>,
    isInvalidCover: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) checkIsValid() },
        value = cover.value,
        isError = isInvalidCover.value,
        onValueChange = {
            cover.value = manageLength(it, 100)
            checkIsValid()
        },
        label = { Text("Cover") },
        supportingText = { Text(messageSupport()) }
    )
}

@Composable
private fun AlbumDescription(
    messageSupport: () -> String,
    checkIsValid: () -> Unit,
    description: MutableState<String>,
    isInvalid: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) checkIsValid() },
        value = description.value,
        isError = isInvalid.value,
        onValueChange = {
            description.value = manageLength(it, 500)
            checkIsValid()
        },
        label = { Text("Description") },
        supportingText = { Text(messageSupport()) },
        minLines = 3,
        maxLines = 3
    )
}

@Composable
private fun AlbumGenre(
    messageSupport: () -> String,
    checkIsValid: () -> Unit,
    optionSelected: MutableState<String>,
    isInvalid: MutableState<Boolean>
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it; },
    ){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            value = optionSelected.value,
            onValueChange = { optionSelected.value = it; checkIsValid() },
            readOnly = true,
            singleLine = true,
            isError = isInvalid.value,
            label = { Text("Genre") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            supportingText = { if (isInvalid.value) Text(messageSupport()) else Unit }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
                checkIsValid()
            },
        ) {
            Genre.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.value, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        optionSelected.value = option.value
                        expanded.value = false
                        checkIsValid()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun AlbumRecordLabel(
    messageSupport: () -> String,
    checkIsValid: () -> Unit,
    optionSelected: MutableState<String>,
    isInvalid: MutableState<Boolean>
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it; },
    ){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = optionSelected.value,
            onValueChange = { optionSelected.value = it; checkIsValid() },
            readOnly = true,
            singleLine = true,
            isError = isInvalid.value,
            label = { Text("Record Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            supportingText = { if (isInvalid.value) Text(messageSupport()) else Unit }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
                checkIsValid()
            },
        ) {
            RecordLabel.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.value, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        optionSelected.value = option.value
                        expanded.value = false
                        checkIsValid()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun AlbumReleaseDatePicker(date: MutableState<LocalDate>) {
    val isOpen = remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = date.value.format(DateTimeFormatter.ISO_DATE),
            label = { Text("Release date") },
            onValueChange = {},
            trailingIcon = {
                IconButton(
                    onClick = { isOpen.value = true } // show de dialog
                ) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar")
                }
            },
            supportingText = { Text("yyyy-MM-dd") }
        )
    }

    if (isOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                isOpen.value = false // close dialog

                if (it != null) { // Set the date
                    date.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                }
            },
            onCancel = {
                isOpen.value = false //close dialog
            }
        )
    }
}

@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()
    val minDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(
            state = state,
            title = { },
            headline = {
                Text(
                    text = "Seleccione una fecha",
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 24.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                    )
                )
            },
            dateValidator = { selectedDate -> selectedDate <= minDate }
        )
    }
}

@Composable
fun TopBar(back: () -> Unit, save: () -> Unit) {
    TopAppBar(title = {
        Text(
            "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
        navigationIcon = {
            IconButton(onClick = back) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
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

private fun manageLength(input: String, size: Int) = if (input.length > size) input.substring(0..<size) else input

@Composable
@Preview(showBackground = true)
fun VistaPrevia() {
    CreateAlbumForm({})
}