@file:OptIn(ExperimentalMaterialApi::class)

package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ErrorScreen
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumViewModel


@Composable
fun AlbumScreen(
    onClickAlbumsDetail: (albumId: String) -> Unit,
    modifier: Modifier = Modifier,
    albumViewModel: AlbumViewModel = viewModel(factory = AlbumViewModel.Factory),
) {
    val albumUiState = albumViewModel.albumUiState
    val isRefreshing = AlbumUiState.Loading == albumUiState
    val pullRefreshState = rememberPullRefreshState(isRefreshing, albumViewModel::refreshAlbums)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (albumUiState) {
                //is AlbumUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is AlbumUiState.Success -> AlbumGridScreen(
                    albumUiState.albums,
                    onClickAlbumsDetail,
                    modifier
                )

                is AlbumUiState.Error -> ErrorScreen(
                    albumViewModel::refreshAlbums,
                    modifier = modifier.fillMaxSize()
                )

                AlbumUiState.Loading -> Unit
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
fun AlbumGridScreen(
    albums: List<AlbumDto>,
    onClickAlbumsDetail: (albumId: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column {
        Text(
            text = stringResource(R.string.albumTitle),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(8.dp)
        )
        if (albums.isEmpty()) {
            Text(
                text = stringResource(R.string.no_data),
                modifier = Modifier.padding(8.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = modifier,
                contentPadding = contentPadding,
            ) {
                items(items = albums, key = { album -> album.id }) { album ->
                    AlbumCard(
                        album,
                        onClickAlbumsDetail = onClickAlbumsDetail,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

    }
}

@Composable
fun AlbumCard(
    album: AlbumDto,
    onClickAlbumsDetail: (albumId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.clickable(onClick = {
            onClickAlbumsDetail(album.id.toString())
        }),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(album.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = album.tracks.size.toString() + stringResource(R.string.tracks),
                )
            }
        }
    }
}

