@file:OptIn(ExperimentalMaterial3Api::class)

package com.vinilos.misw4203.grupo6_202412.view.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.models.dto.CommentDto
import com.vinilos.misw4203.grupo6_202412.models.dto.TraksDto
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarDisable
import com.vinilos.misw4203.grupo6_202412.ui.theme.StarEnable
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ExpandableText
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AlbumScreenDetail(
    idDetail: String,
    onClick: () -> Unit
) {
    val albumDto = AlbumDetail.albumDto
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { TopBarAlbumDetail(onClick) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            AlbumDetailContent(albumDto)
        }
    }
}

@Composable
fun TopBarAlbumDetail(onClick: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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

@Composable
fun AlbumDetailContent(albumDto: AlbumDto) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        AlbumCover(albumDto)
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
fun AlbumCover(albumDto: AlbumDto) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.22f),
    ) {
        ImageAsync(
            url = albumDto.cover ?: "",
            contentDescription = albumDto.name,
            modifier = Modifier
                .width(140.dp)
                .clip(RoundedCornerShape(20.dp)),
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = albumDto.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "${albumDto.tracks.size} Canciones",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Comentar álbum")
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
            Text(
                text = "Publicado $formattedDate",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = albumDto.recordLabel ?: "", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "Genero ${albumDto.genre ?: ""}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.padding(4.dp))
        ExpandableText(
            fontSize = 14.sp,
            text = albumDto.description ?: "",
            modifier = Modifier.padding(3.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Performers(performers: List<ArtistDto>) {
    Text(text = "Artistas", style = MaterialTheme.typography.titleMedium)
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
    Text(text = "Canciones", style = MaterialTheme.typography.titleMedium)
    tracks.forEach { track ->
        ChipTrack(track)
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
    Text(text = "Comentarios", style = MaterialTheme.typography.titleMedium)
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
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint =  if (i >= rating) StarDisable else StarEnable
            )
        }
    }
}

object AlbumDetail {
    val albumDto = AlbumDto(
        id = 100,
        name = "Buscando América",
        cover = "https://cdn.shopify.com/s/files/1/0275/3095/products/image_4931268b-7acf-4702-9c55-b2b3a03ed999_1024x1024.jpg",
        releaseDate = "1984-08-01T00:00:00.000Z",
        description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
        genre = "Salsa",
        recordLabel = "Elektra",
        tracks = arrayListOf(
            TraksDto(
                id = 100,
                name = "Buscando América",
                duration = "00:04:00",
            ),
            TraksDto(
                id = 101,
                name = "Poeta del pueblo",
                duration = "00:04:00"
            )
        ),
        performers = arrayListOf(
            ArtistDto(
                id = 100,
                name = "Rubén Blades",
                image = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                description = "Rubén Blades Bellido de Luna (Ciudad de Panamá, 16 de julio de 1948) es un cantante, compositor, músico, actor, abogado, político y activista panameño, figura prominente de la música latina y la salsa. Ha ganado nueve premios Grammy y cinco premios Grammy Latino.",
                birthDate = "1948-07-16T00:00:00.000Z"
            ),
            ArtistDto(
                id = 100,
                name = "Queen",
                image = "https://pm1.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg",
                description = "Queen es una banda británica de rock formada en 1970 en Londres por el cantante Freddie Mercury, el guitarrista Brian May, el baterista Roger Taylor y el bajista John Deacon. Si bien el grupo ha presentado bajas de dos de sus miembros (Mercury, fallecido en 1991, y Deacon, retirado en 1997), los integrantes restantes, May y Taylor, continúan trabajando bajo el nombre Queen, por lo que la banda aún se considera activa.",
                birthDate = "1948-07-16T00:00:00.000Z"
            ),
            ArtistDto(
                id = 100,
                name = "Rubén Blades",
                image = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                description = "Rubén Blades Bellido de Luna (Ciudad de Panamá, 16 de julio de 1948) es un cantante, compositor, músico, actor, abogado, político y activista panameño, figura prominente de la música latina y la salsa. Ha ganado nueve premios Grammy y cinco premios Grammy Latino.",
                birthDate = "1970-01-01T00:00:00.000Z"
            )
        ),
        comments = arrayListOf(
            CommentDto(
                id = 100,
                description = "The most relevant album of Ruben Blades",
                rating = 4,
            ),
            CommentDto(
                id = 100,
                description = "lorem  ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                rating = 3,
            )
        )
    )
}

fun formatDateString(inputDate: String, inputFormat: String, outputFormat: String): String {
    val originalFormat = SimpleDateFormat(inputFormat, Locale.US)
    val targetFormat = SimpleDateFormat(outputFormat, Locale.US)

    val date = originalFormat.parse(inputDate)
    val formattedDate = date?.let { targetFormat.format(it) }

    return formattedDate ?: ""
}

//preview AlbumScreenDetail
@Preview()
@Composable
fun AlbumScreenDetailPreview() {
    AlbumScreenDetail("100", {})
}