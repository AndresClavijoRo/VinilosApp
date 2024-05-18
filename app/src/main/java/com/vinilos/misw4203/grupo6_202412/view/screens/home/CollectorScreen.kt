package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ErrorScreen
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorViewModel

@Composable
fun CollectorList(onCollectorClick: (String) -> Unit, collectorList: List<CollectorDto>) {
    if (collectorList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = stringResource(R.string.noCollectors),
                style = MaterialTheme.typography.titleMedium
            )
        }

    } else {
        LazyColumn {
            itemsIndexed(collectorList) { _, collector ->
                CollectorListItem(collector, onCollectorClick)
            }
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
            .clickable { onCollectorClick("${collector.id}") }
            .testTag("collectorListItem"),
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = collector.name ?: "Default name",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = collector.email ?: "Default email",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        leadingContent = {
            ImageAsync(
                "https://ui-avatars.com/api/?name=${collector.name}&size=256&background=random&rounded=true&color=fff",
                "",
                Modifier
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.medium),
                ContentScale.Crop
            )
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectorScreen(
    onClick: (collectorId: String) -> Unit,
    modifier: Modifier = Modifier,
    collectorViewModel: CollectorViewModel = viewModel(factory = CollectorViewModel.Factory),
) {
    val collectors = collectorViewModel.collectorsState.value
    val isLoading = collectorViewModel.isLoading.value
    val errorText = collectorViewModel.errorText.value
    val pullRefreshState =
        rememberPullRefreshState(isLoading, collectorViewModel::getAllCollectors)

    Scaffold(

    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(it)
        ) {
            when {
                isLoading -> Unit
                errorText != null -> ErrorScreen(
                    modifier = modifier.fillMaxSize(),
                    onClickRefresh = { collectorViewModel.getAllCollectors() },
                    message = errorText
                )

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.collectorTitle),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    CollectorList(onClick, collectors)
                }
            }
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .testTag("pullRefreshIndicator"),
            )
        }

    }


}