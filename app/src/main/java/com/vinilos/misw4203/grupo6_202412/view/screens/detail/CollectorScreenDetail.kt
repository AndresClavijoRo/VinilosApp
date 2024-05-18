@file:OptIn(ExperimentalMaterial3Api::class)

package com.vinilos.misw4203.grupo6_202412.view.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ErrorScreen
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.view.uiControls.PerformersChips
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorAlbumsUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorDetailViewModel
import com.vinilos.misw4203.grupo6_202412.viewModel.CollectorUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectorScreenDetail(
    idDetail: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    collectorScreenModel: CollectorDetailViewModel = viewModel<CollectorDetailViewModel>(
        factory = CollectorDetailViewModel.Factory(idDetail.toInt()),
        key = idDetail,
    ),
) {

    val collectorDetailState = collectorScreenModel.collectorUiState
    val isRefreshing = collectorDetailState == CollectorUiState.Loading
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        collectorScreenModel.fetchData()
    })

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { TopBarCollectorDetail(scrollBehavior, onClickBack) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(it)
        ) {
            when (collectorDetailState) {
                is CollectorUiState.Success -> CollectorDetailContent(
                    collectorDetailState.collector,
                    collectorScreenModel.collectoralbumsUiState,
                )

                is CollectorUiState.Error -> {
                    ErrorScreen(
                        modifier = modifier.fillMaxSize(),
                        onClickRefresh = { collectorScreenModel.fetchData() },
                    )
                }

                is CollectorUiState.Loading -> Unit
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
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


@Composable
fun CollectorInfo(collector: CollectorDto) {
    Column {

        Row {
            Text(
                text = stringResource(R.string.collectorPhone),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = collector.telephone ?: "Default phone",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row {
            Text(
                text = stringResource(R.string.collectorEmail),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = collector.email ?: "Default email",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AlbumsList(albums: List<AlbumDto>) {
    if (albums.isEmpty()) {
        Text(
            text = stringResource(R.string.collectorNoAlbums),
            modifier = Modifier.padding(8.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(100.dp, 200.dp)

        ) {
            itemsIndexed(albums) { _, album ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("collectorAlbumItem"),
                    headlineContent = { Text(album.name) },
                    leadingContent = {
                        Row(
                            modifier = Modifier
                        ) {
                            ImageAsync(
                                "${album.cover}",
                                "",
                                Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                ContentScale.Crop
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AlbumsSection(albumsState: CollectorAlbumsUiState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.albums),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        when (albumsState) {
            is CollectorAlbumsUiState.Success -> {
                AlbumsList(albumsState.collectorAlbums)
            }

            is CollectorAlbumsUiState.Error -> {
                Text(
                    text = stringResource(R.string.errorLoadingAlbums),
                    modifier = Modifier.padding(8.dp)
                )
            }

            is CollectorAlbumsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(8.dp).testTag("loadingAlbums"))
            }
        }
    }
}

@Composable
fun FavoriteArtis(artists: ArrayList<ArtistDto>) {
    if (artists.isNotEmpty()) {
        PerformersChips(artists)
    } else {
        Text(
            text = stringResource(R.string.collectorNoFavoriteArtists),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CollectorDetailContent(collector: CollectorDto, albumsState: CollectorAlbumsUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = (collector.name ?: "Default name"),
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        CollectorInfo(collector = collector)
        Spacer(modifier = Modifier.padding(8.dp))
        AlbumsSection(albumsState)
        Spacer(modifier = Modifier.padding(8.dp))
        FavoriteArtis(collector.favoritePerformers)
    }
}


@Composable
fun TopBarCollectorDetail(
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}