@file:OptIn(ExperimentalMaterial3Api::class)
package com.vinilos.misw4203.grupo6_202412.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment


@Composable
fun CreateAlbumForm(back: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var cover by rememberSaveable { mutableStateOf("") }
    var releaseDate by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var recordLabel by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = { TopBar(back, {}) }) {
        Column(
            modifier = Modifier.padding(it).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {name = it},
                label = { Text("Name") },
            )

            OutlinedTextField(
                value = cover,
                onValueChange = {cover = it},
                label = { Text("Cover") },
            )

            OutlinedTextField(
                value = releaseDate,
                onValueChange = {releaseDate = it},
                label = { Text("Release Date") },
            )

            OutlinedTextField(
                value = genre,
                onValueChange = {genre = it},
                label = { Text("Genre") },
            )

            OutlinedTextField(
                value = recordLabel,
                onValueChange = {recordLabel = it},
                label = { Text("Record Label") },
            )

            OutlinedTextField(
                value = description,
                onValueChange = {description = it},
                label = { Text("Description") },
                supportingText = {Text("Max 500 characters")}
            )

        }
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
            IconButton(onClick = { back() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            TextButton(onClick = { save() }) {
                Text(text = "Save")
            }
        }
    )
}



@Composable
@Preview(showBackground = true)
fun VistaPrevia(){
    CreateAlbumForm({})

}