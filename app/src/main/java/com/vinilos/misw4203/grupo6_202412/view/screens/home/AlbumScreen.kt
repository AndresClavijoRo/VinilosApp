package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vinilos.misw4203.grupo6_202412.R

@Composable
fun AlbumScreen(
    onClickAlbumsDetail: (albumId:String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Text(
            text = stringResource(R.string.albumTitle),
            style = MaterialTheme.typography.displaySmall,
        )
        Button(onClick = {
            onClickAlbumsDetail("1")
        }) {
            Text(text = "Go to Artist Detail")
        }
    }
}
