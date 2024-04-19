package com.vinilos.misw4203.grupo6_202412.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vinilos.misw4203.grupo6_202412.view.screens.home.HomeScreen
import com.vinilos.misw4203.grupo6_202412.view.screens.login.LoginScreen


@Composable
fun RootNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Graph.LOGIN
    ){
        composable(route = Graph.LOGIN) {
            LoginScreen(
                onClick = { navController.navigate(Graph.HOME) }
            )
        }

        composable(route = Graph.HOME) {
            HomeScreen(
                onClickAlbumsDetail = { albumId ->
                    navController.navigate(GraphDetail.ALBUMS_DETAIL.replace("{albumId}", albumId))                },
                onClickArtistsDetail = { performerId ->
                    navController.navigate(GraphDetail.PERFORMER_DETAIL.replace("{performerId}", performerId))  },
                onClickCollectorsDetail = { navController.navigate(GraphDetail.COLLECTORS_DETAIL) }
            )
        }

        detailNavGraph(navController = navController)
    }
}


object Graph {
    const val LOGIN = "login"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}

