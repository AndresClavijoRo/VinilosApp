@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.vinilos.misw4203.grupo6_202412.view.screens.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import com.vinilos.misw4203.grupo6_202412.models.dto.TraksDto
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarDisable
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarEnable
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ErrorScreen
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ExpandableText
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.view.utils.formatDateString
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumDetailUiState
import com.vinilos.misw4203.grupo6_202412.viewModel.AlbumDetailViewModel

@Composable
fun AlbumScreenDetail(
    idDetail: String,
    onClickBack: () -> Unit,
    onClickCommentAlbum: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    albumDetailViewModel: AlbumDetailViewModel = viewModel<AlbumDetailViewModel>(
        factory = AlbumDetailViewModel.Factory(idDetail.toInt()),
        key = idDetail,
    ),
) {
    val albumDetailUiState = albumDetailViewModel.albumDetailUiState
    val isRefreshing = albumDetailUiState == AlbumDetailUiState.Loading
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        albumDetailViewModel.getAllAlbumById(idDetail.toInt())
    })

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { TopBarAlbumDetail(scrollBehavior, onClickBack) },
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
            when (albumDetailUiState) {
                is AlbumDetailUiState.Success -> AlbumDetailContent(
                    onClickCommentAlbum,
                    albumDetailUiState.album
                )
                is AlbumDetailUiState.Error -> {
                        ErrorScreen(
                            modifier = modifier.fillMaxSize(),
                            onClickRefresh = { albumDetailViewModel.getAllAlbumById(idDetail.toInt()) },
                        )
                }
                is AlbumDetailUiState.Loading -> Unit
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
fun TopBarAlbumDetail(
    scrollBehavior: TopAppBarScrollBehavior,
    onClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
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

@Composable
fun AlbumDetailContent(
    comentarAlbum: (id: String) -> Unit,
    albumDto: AlbumDto
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        AlbumCover(albumDto, comentarAlbum)
        Spacer(modifier = Modifier.padding(8.dp))
        AlbumInfo(albumDto)
        Spacer(modifier = Modifier.padding(8.dp))
        Performers(albumDto.performers)
        Spacer(modifier = Modifier.padding(8.dp))
        Tracks(albumDto.tracks)
        Spacer(modifier = Modifier.padding(8.dp))
        Comments(albumDto.comments)
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun AlbumCover(
    albumDto: AlbumDto,
    comentarAlbum: (id: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
    ) {
        ImageAsync(
            url = albumDto.cover ?: "",
            contentDescription = albumDto.name,
            modifier = Modifier
                .width(140.dp)
                .size(140.dp)
                .clip(RoundedCornerShape(20.dp)),
             contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = albumDto.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "${albumDto.tracks.size} ${stringResource(R.string.tracksTitle)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = { comentarAlbum(albumDto.id.toString()) }) {
                Text(text = stringResource(R.string.btn_album_comment))
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun AlbumInfo(albumDto: AlbumDto) {

    val formattedDate =
        if (albumDto.releaseDate != null) formatDateString(
            albumDto.releaseDate!!,
            inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            outputFormat = "dd/MM/yyyy"
        )
        else ""

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = stringResource(R.string.publish),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " $formattedDate",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(text = albumDto.recordLabel ?: "", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row {
            Text(
                text = stringResource(R.string.genre),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = albumDto.genre ?: "", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        ExpandableText(
            fontSize = 14.sp,
            text = albumDto.description ?: "",
            modifier = Modifier
                .padding(3.dp)
                .testTag("albumDescription")
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Performers(performers: List<ArtistDto>) {
    Text(
        text = stringResource(id = R.string.performerTitle),
        style = MaterialTheme.typography.titleMedium
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        performers.forEach { performer ->
            PerformerChip(performer)
        }
    }
}


@Composable
fun PerformerChip(performer: ArtistDto) {
    ElevatedAssistChip(
        onClick = { },
        label = { Text(text = performer.name ?: "") },
        leadingIcon = {
            ImageAsync(
                url = performer.image ?: "",
                contentDescription = performer.name ?: "",
                modifier = Modifier
                    .width(18.0.dp)
                    .size(AssistChipDefaults.IconSize)
                    .clip(CircleShape)
            )
        },

        shape = RoundedCornerShape(50.dp),
        border = AssistChipDefaults.assistChipBorder(
            borderColor = Color.Black,
            disabledBorderColor = Color.Gray
        ),
    )
}

@Composable
fun Tracks(tracks: List<TraksDto>) {
    Text(text = stringResource(id = R.string.tracksTitle), style = MaterialTheme.typography.titleMedium)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tracks.windowed(size = 2, step = 2, partialWindows = true).forEach { trackPair ->
            if (trackPair.size == 2) {
                val track1 = trackPair[0]
                val track2 = trackPair[1]
                Column {
                    ChipTrack(track1)
                    ChipTrack(track2)
                }
            } else {
                val track1 = trackPair[0]
                Column {
                    ChipTrack(track1)
                }
            }
        }
    }
}

@Composable
fun ChipTrack(track: TraksDto) {
    ElevatedAssistChip(
        onClick = { },
        label = {
            Text(
                text = "${track.name} - ${track.duration}",
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Album,
                contentDescription = null,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        border = AssistChipDefaults.assistChipBorder(
            borderColor = Color.Black,
            disabledBorderColor = Color.Gray
        ),
    )
}

@Composable
fun Comments(comments: List<CommentDto>) {
    Text(
        text = stringResource(id = R.string.comments),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.padding(4.dp))
    comments.forEach { comment ->
        CommentChip(comment)
    }
}

@Composable
fun CommentChip(comment: CommentDto) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            ExpandableText(
                fontSize = 14.sp,
                text = comment.description ?: "",
                collapsedMaxLine = 2,
                modifier = Modifier.padding(16.dp)
            )
        }
        RatingComment(comment.rating ?: 0)
        Spacer(modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun RatingComment(rating: Int) {
    Row(modifier = Modifier.testTag("ratingComment") )  {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription =  if (i > rating) "" else stringResource(R.string.estrella_comment_ok),
                modifier = Modifier.size(30.dp),
                tint = if (i > rating) StarDisable else StarEnable
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAlbumDetailScreen() {
    AlbumScreenDetail(
        idDetail = "1",
        onClickBack = {},
        onClickCommentAlbum = {})
}