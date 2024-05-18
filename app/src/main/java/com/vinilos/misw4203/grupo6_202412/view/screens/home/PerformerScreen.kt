package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vinilos.misw4203.grupo6_202412.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.platform.testTag
import androidx.compose.material3.Scaffold
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PerformerScreen(
    onClick: (performerId:String) -> Unit,
    performerViewModel: PerformerViewModel = viewModel(factory = PerformerViewModel.Factory)) {
    val performers = performerViewModel.performersState.value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = performerViewModel.isLoading,
        onRefresh = performerViewModel::getAllPerformers
    )
    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .pullRefresh(pullRefreshState)){
            if(!performerViewModel.isLoading && performers.isEmpty()){
                NoDataFound()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize().testTag("PerformerTitle")) {
                if(!performerViewModel.isLoading){
                    Text(
                        text = stringResource(R.string.performerTitle),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                LazyColumn( modifier = Modifier.testTag("artistTag")) {
                    itemsIndexed(performers) { _, artist: ArtistDto ->
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

            PullRefreshIndicator(
                refreshing = performerViewModel.isLoading,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
fun NoDataFound(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(
                text = "NO Data Found",
                style = MaterialTheme.typography.displaySmall,
            )
    }
}

