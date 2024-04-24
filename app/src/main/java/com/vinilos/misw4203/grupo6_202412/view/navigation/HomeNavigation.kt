@file:OptIn(ExperimentalMaterial3Api::class)
package com.vinilos.misw4203.grupo6_202412.view.navigation


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vinilos.misw4203.grupo6_202412.view.screens.home.AlbumScreen
import com.vinilos.misw4203.grupo6_202412.view.screens.home.CollectorScreen
import com.vinilos.misw4203.grupo6_202412.view.screens.home.PerformerScreen

@Composable
fun HomeNavigation(
    onClickAlbumsDetail: (albumId: String) -> Unit,
    onClickArtistsDetail: (performerId: String) -> Unit,
    onClickCollectorsDetail: () -> Unit,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = GraphHome.ALBUMS,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) }
    ) {
        composable(
            route = GraphHome.ALBUMS,
        ) {
            AlbumScreen(
                onClickAlbumsDetail = onClickAlbumsDetail,
            )
        }
        composable(route = GraphHome.PERFORMERS) {
            PerformerScreen(
                onClick = onClickArtistsDetail,
            )
        }
        composable(route = GraphHome.COLLECTORS) {
            CollectorScreen(
                onClick = {
                    onClickCollectorsDetail()
                }
            )
        }
    }
}

object GraphHome {
    const val ALBUMS = "login"
    const val PERFORMERS = "performers"
    const val COLLECTORS = "collectors"
}
