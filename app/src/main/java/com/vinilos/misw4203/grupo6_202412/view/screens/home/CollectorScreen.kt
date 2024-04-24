package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerViewModel

@Composable
fun CollectorList(onCollectorClick: (String)->Unit, collectorList: List<CollectorDto>){
    LazyColumn {
        itemsIndexed(collectorList) { i, collector ->
            CollectorListItem(collector , onCollectorClick )
        }
    }
}

@Composable
fun CollectorListItem(
    collector: CollectorDto,
    onCollectorClick: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCollectorClick("${collector.id}") },
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = collector.name ?: "Default name", style = MaterialTheme.typography.titleSmall)
                    Text(text = collector.email ?: "Default email", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        leadingContent = {
            ImageAsync(
                "https://ui-avatars.com/api/?name=${collector.name}&size=256&background=random&rounded=true&color=fff",
                collector.name ?: "Default name",
                Modifier.size(40.dp).clip(MaterialTheme.shapes.medium),
                ContentScale.Crop
            )
        }
    )
}
@Composable
fun CollectorScreen(
    onClick: (collectorId:String) -> Unit
) {
    val collectorViewModel: CollectorViewModel = viewModel(factory = CollectorViewModel.Factory)
    val collectors = collectorViewModel.collectorsState.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.collectorTitle),
            style = MaterialTheme.typography.displaySmall,
        )
        CollectorList(onClick, collectors)
    }

}