@file:OptIn(ExperimentalMaterial3Api::class)
package com.vinilos.misw4203.grupo6_202412.view.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vinilos.misw4203.grupo6_202412.R
import com.vinilos.misw4203.grupo6_202412.view.navigation.HomeNavigation
import com.vinilos.misw4203.grupo6_202412.view.navigation.currentRoute

@Composable
fun HomeScreen(
    onClickAlbumsDetail: (albumId:String) -> Unit,
    onClickArtistsDetail: (performerId: String) -> Unit,
    onClickCollectorsDetail: (collectorId: String) -> Unit,
    onClickCreateAlbum: () -> Unit,
){
    val navHomeController = rememberNavController()
    val currentRoute = currentRoute(navHomeController)
    val screens = listOf(
        BottomScreens.Albums,
        BottomScreens.Artists,
        BottomScreens.Collectors
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBarHome(scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            BottomBar(
                navController = navHomeController,
                currentRoute,
                screens
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            HomeNavigation(
                onClickAlbumsDetail,
                onClickArtistsDetail,
                onClickCollectorsDetail,
                onClickCreateAlbum,
                navController = navHomeController
            )
        }
    }
}

@Composable
fun TopBarHome(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondary,
        ),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String,
    screensHome: List<BottomScreens>
) {
    BottomAppBar(

    ) {
        NavigationBar() {
            screensHome.forEach { screen ->
                val isSelected = screen.route == currentRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(screen.iconResourceId),
                            contentDescription = stringResource(id = screen.titleResourceId),
                        )
                    },
                    label = {
                        Text(text = stringResource(id = screen.titleResourceId))
                    }
                )
            }
        }
    }
}