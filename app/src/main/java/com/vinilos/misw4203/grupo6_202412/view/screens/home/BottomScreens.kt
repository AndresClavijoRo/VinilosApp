package com.vinilos.misw4203.grupo6_202412.view.screens.home

import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.view.navigation.GraphHome

sealed class BottomScreens(
    val route: String,
    val titleResourceId: Int,
    val iconResourceId: Int
) {
    object Albums : BottomScreens(
        route = GraphHome.ALBUMS,
        titleResourceId = R.string.albums,
        iconResourceId = R.drawable.outline_media_output_24
    )

    object Artists : BottomScreens(
        route = GraphHome.PERFORMERS,
        titleResourceId = R.string.performers,
        iconResourceId = R.drawable.outline_artist_24
    )

    object Collectors : BottomScreens(
        route = GraphHome.COLLECTORS,
        titleResourceId = R.string.collectors,
        iconResourceId = R.drawable.outline_spatial_audio_24
    )
}

