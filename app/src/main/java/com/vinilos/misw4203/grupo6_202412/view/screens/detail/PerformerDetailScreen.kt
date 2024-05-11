@file:OptIn(ExperimentalMaterial3Api::class)
package com.vinilos.misw4203.grupo6_202412.view.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vinilos.misw4203.grupo6_202412.models.dto.AlbumDto
import com.vinilos.misw4203.grupo6_202412.view.uiControls.ImageAsync
import com.vinilos.misw4203.grupo6_202412.models.dto.ArtistDto
import com.vinilos.misw4203.grupo6_202412.viewModel.PerformerDetailViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PerformerDetailScreen(
    performerId: String,
    onClick: () -> Unit,
    performerDetailViewModel: PerformerDetailViewModel = viewModel(factory = PerformerDetailViewModel.Factory)) {

    performerDetailViewModel.getPerformerById(performerId.toInt())
    val performerDetail = performerDetailViewModel.performerDetailState
    Scaffold(
        topBar = { TopBarPerformerDetail(onClick) }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = "${performerDetail?.name}",
                modifier = Modifier.padding(bottom = 20.dp, start = 8.dp, top = 30.dp).fillMaxWidth().
                testTag("PerformerDetailTitle"),
                style = MaterialTheme.typography.titleLarge
            )
            PerformerDetail(performerDetail)
        }
    }
}

@Composable
fun PerformerDetail(performerDetail: ArtistDto?){
    val formattedDate = parseCustomDate(performerDetail?.birthDate)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(horizontal = 40.dp)),
    ){

        ImageAsync(
            "${performerDetail?.image}",
            "${performerDetail?.id}",
            Modifier
                .align(Alignment.CenterHorizontally)
                .aspectRatio(1f).padding(PaddingValues(horizontal = 40.dp, vertical = 10.dp))
                .clip(RoundedCornerShape(15.dp)),
            ContentScale.Crop
        )
        Text(
            text = "Fecha Nacimiento  $formattedDate",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp).testTag("birthDateTag"),
        )
        Text(
            text = "${performerDetail?.description}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 10.dp).testTag("descriptionTag")
        )
        Text(
            text = "Albums",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        performerDetail?.albums?.let { AlbumsPerformer(albums = it) }
    }
}

private fun parseCustomDate(birthDate: String?): String{
    return if(birthDate == null){
      "No Data"
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val parsedDate = LocalDate.parse(birthDate.toString(), formatter)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        outputFormatter.format(parsedDate)
    }
}

@Composable
fun AlbumsPerformer(albums: ArrayList<AlbumDto>){
    Column() {
        if(albums.isEmpty()){
            NoDataFound()
        } else {
            LazyColumn( modifier = Modifier.testTag("albumChildTag")) {
                items(albums) { album ->
                    ListItem(
                        headlineContent = {  Text("${album.name}")},
                        leadingContent = {
                            Row( modifier = Modifier
                            ) {
                                ImageAsync(
                                    "${album.cover}",
                                    "${album.recordLabel}",
                                    Modifier.
                                    size(100.dp)
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
}


@Composable
fun NoDataFound(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Albums Found",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
fun TopBarPerformerDetail(onClick: () -> Unit){
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