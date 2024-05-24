@file:OptIn(ExperimentalMaterial3Api::class)

package com.vinilos.misw4203.grupo6_202412.view.screens.detail

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CollectorDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ErrorScreen
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.view.uiControls.PerformersChips
import com.vinilos.misw4203.grupo6_202412.viewModel.ArtistsListUiState
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
    val context = LocalContext.current

    LaunchedEffect(true) {
        collectorScreenModel.showToastMessage.collect { show ->
            if (show) {
                Toast.makeText(context, "Artista agregado como favorito", Toast.LENGTH_SHORT).show()
            }
        }
    }
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
                    collectorDetailState.collector, collectorScreenModel
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
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(8.dp)
                        .testTag("loadingAlbums")
                )
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

private fun filtrarOpciones(
    options: List<ArtistDto>, textStateCollector: String
) = options.filter { collector ->
    collector.name!!.trim().contains(textStateCollector.trim(), ignoreCase = true)
}.take(5)

@Composable
fun ArtistSelectField(
    options: List<ArtistDto>,
    musicianToAdd: MutableState<ArtistDto?>
) {

    var filteredOptions: List<ArtistDto> by remember { mutableStateOf(options) }
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val textStateMusician = remember { mutableStateOf(TextFieldValue("")) }
    val expanded = allowExpanded && filteredOptions.isNotEmpty()
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(0.75f),
            value = textStateMusician.value,
            onValueChange = { newValue ->
                textStateMusician.value = newValue
                filteredOptions = if (textStateMusician.value.text.isNotEmpty()) {
                    filtrarOpciones(options, textStateMusician.value.text)
                } else {
                    options
                }
                if (filteredOptions.isNotEmpty()) {
                    setExpanded(true)
                }
            },
            singleLine = true,
            label = { Text(stringResource(R.string.muscian)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                if (musicianToAdd.value == null) {
                    val firstMusician = filteredOptions.firstOrNull()
                    musicianToAdd.value = firstMusician
                    textStateMusician.value = TextFieldValue(
                        text = firstMusician?.name!!,
                        TextRange(firstMusician.name!!.length, firstMusician.name!!.length)
                    )
                    filteredOptions = filtrarOpciones(options, textStateMusician.value.text)
                    setExpanded(false)
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
                        textStateMusician.value = TextFieldValue(
                            text = option.name!!,
                            TextRange(option.name!!.length, option.name!!.length)
                        )
                        musicianToAdd.value = option
                        filteredOptions = filtrarOpciones(options, textStateMusician.value.text)
                        setExpanded(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun AddFavoriteArtistsFiled(
    toggleShowFavoriteArtist: () -> Unit,
    options: List<ArtistDto>,
    musicianToAdd: MutableState<ArtistDto?>,
    addFavoriteArtists: () -> Unit
) {
    val hasMusicianSelected = musicianToAdd.value != null
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArtistSelectField(options = options, musicianToAdd)
        IconButton(
            onClick = toggleShowFavoriteArtist,
            modifier = Modifier.testTag("closeFavoriteArtist")
        ) {
            Icon(
                imageVector = Icons.Rounded.RemoveCircleOutline,
                contentDescription = stringResource(R.string.cancelAddFavoriteArtist),
                tint = MaterialTheme.colorScheme.error
            )
        }
        if (hasMusicianSelected) {
            IconButton(
                onClick = addFavoriteArtists, modifier = Modifier.testTag("saveFavoriteArtist")
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircleOutline,
                    contentDescription = stringResource(R.string.confirmAddFavoriteArtist),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AddFavoriteArtist(
    showAddFavoriteArtists: MutableState<Boolean>,
    artistsListUiState: ArtistsListUiState,
    toggleShowFavoriteArtist: () -> Unit,
    addFavoriteArtists: () -> Unit,
    collector: CollectorDto,
    musicianToAdd: MutableState<ArtistDto?>
) {

    when (artistsListUiState) {
        is ArtistsListUiState.Success -> {
            val pendingArtists = artistsListUiState.artists.filter { artist ->
                !collector.favoritePerformers.any { it.id == artist.id }
            }
            if (pendingArtists.isEmpty()) {
                return
            }
            if (showAddFavoriteArtists.value) {
                AddFavoriteArtistsFiled(
                    toggleShowFavoriteArtist = toggleShowFavoriteArtist,
                    options = pendingArtists,
                    musicianToAdd = musicianToAdd,
                    addFavoriteArtists = addFavoriteArtists
                )
            } else {
                Button(
                    onClick = toggleShowFavoriteArtist,
                ) {
                    Text(text = stringResource(R.string.addFavoriteArtist))
                }
            }
        }

        is ArtistsListUiState.Error -> {
            Text(
                text = artistsListUiState.message,
                modifier = Modifier.padding(8.dp)
            )
        }

        is ArtistsListUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .testTag("loadingArtists")
            )
        }
    }

}

@Composable
fun CollectorDetailContent(collector: CollectorDto, viewModel: CollectorDetailViewModel) {
    val albumsState = viewModel.collectoralbumsUiState
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
        Spacer(modifier = Modifier.padding(4.dp))
        AddFavoriteArtist(
            viewModel.showAddFavoriteArtists,
            viewModel.artistsListUiState,
            { viewModel.toggleShowFavoriteArtist() },
            { viewModel.addFavoriteArtists() },
            collector,
            viewModel.musicianToAdd
        )
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
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}