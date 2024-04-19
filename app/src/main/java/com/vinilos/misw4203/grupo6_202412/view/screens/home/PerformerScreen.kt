package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vinilos.misw4203.grupo6_202412.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerViewModel.Companion.Factory


@Composable
fun PerformerScreen(
    onClick: (performerId:String) -> Unit) {
    val performerViewModel: PerformerViewModel = viewModel(factory = Factory)
    val performers = performerViewModel.performersState.value
    Column(modifier = Modifier.padding(start = 50.dp)) {
        Text(
            text = stringResource(R.string.performerTitle),
            style = MaterialTheme.typography.displaySmall,
        )
        LazyColumn() {
            itemsIndexed(items =  performers) { index, artist ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick("${artist.id}")
                        },
                    headlineContent = { Text("${artist.name}") },
                    leadingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ImageAsync(
                                "${artist.image}",
                                "",
                                Modifier.size(90.dp).clip(MaterialTheme.shapes.medium),
                                ContentScale.Crop
                                )
                        }
                    }
                )
            }
        }
    }
}

