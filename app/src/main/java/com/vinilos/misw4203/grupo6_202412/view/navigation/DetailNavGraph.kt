package com.vinilos.misw4203.grupo6_202412.view.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.AlbumScreenDetail
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.CollectorScreenDetail
import com.vinilos.misw4203.grupo6_202412.view.screens.detail.PerformerScreenDetail

fun NavGraphBuilder.detailNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = GraphDetail.ALBUMS_DETAIL
    ) {
        composable(route = GraphDetail.ALBUMS_DETAIL) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId")
            AlbumScreenDetail(
                idDetail = albumId ?: "",
                onClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = GraphDetail.PERFORMER_DETAIL) { backStackEntry ->
            val performerId = backStackEntry.arguments?.getString("performerId")
            PerformerScreenDetail(
                performerId = performerId ?: "",
                onClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = GraphDetail.COLLECTORS_DETAIL) { backStackEntry ->
            val collectorId = backStackEntry.arguments?.getString("collectorId")
            CollectorScreenDetail(
                idDetail = collectorId ?: "",
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

object GraphDetail {
    const val ALBUMS_DETAIL = "album_detail/{albumId}"
    const val PERFORMER_DETAIL = "performer_detail/{performerId}"
    const val COLLECTORS_DETAIL = "collectors_detail/{collectorId}"
}
