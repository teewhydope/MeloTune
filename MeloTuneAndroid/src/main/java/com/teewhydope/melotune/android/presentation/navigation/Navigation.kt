package com.teewhydope.melotune.android.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.teewhydope.melotune.android.presentation.home.AppHome
import com.teewhydope.melotune.android.presentation.song_list.SongListViewModel
import kotlinx.coroutines.InternalCoroutinesApi


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
internal fun Navigation(
    hasSoftKey: Boolean,
    navController: NavHostController,
    width: Int,
    imageLoader: ImageLoader,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        builder = {
            addHomeTopLevel(
                navController = navController,
                width = width,
                imageLoader = imageLoader,
                hasSoftKey = hasSoftKey,
            )
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addHomeTopLevel(
    navController: NavHostController,
    width: Int,
    imageLoader: ImageLoader,
    hasSoftKey: Boolean,
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        addHome(
            navController = navController,
            imageLoader = imageLoader,

            )
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addHome(
    navController: NavHostController,
    imageLoader: ImageLoader,

    ) {
    //Animation Settings
    composable(
        route = LeafScreen.Home.route,
    ) {
        val viewModel: SongListViewModel = hiltViewModel()
        AppHome(
            navController = navController,
            state = viewModel.state.value,
            onTriggerEvent = viewModel::onTriggerEvent,
            imageLoader = imageLoader,
        )
    }
}


/*
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addAlbumPage() {
    //Animation Settings
    composable(
        route = LeafScreen.AlbumPage.route,
    ) {

        AlbumListScreen()
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addArtistsPage() {
    //Animation Settings
    composable(
        route = LeafScreen.ArtistsPage.route,
    ) {
        ArtistsListScreen()
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addPlaylistsPage() {
    //Animation Settings
    composable(
        route = LeafScreen.PlaylistsPage.route
    ) {
        PlayListScreen()
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addFavoritePage() {
    //Animation Settings
    composable(
        route = LeafScreen.FavoritePage.route,
    ) {
        FavoritesListScreen()
    }
}*/



