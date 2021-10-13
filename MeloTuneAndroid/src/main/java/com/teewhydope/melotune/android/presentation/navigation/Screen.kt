package com.teewhydope.melotune.android.presentation.navigation

sealed class Screen(val route: String, val name: String) {
    object Home : Screen("homeRoot", "Home")
}


sealed class LeafScreen(
    val route: String,
) {
    object Home : LeafScreen(route = "home")

    object SongList : LeafScreen(route = "songList")

    object Player : LeafScreen(route = "player/{playerId}") {
        fun createRoute(playerId: Int): String = "player/$playerId"
    }

    object AlbumPage : LeafScreen(route = "albumPage")
    object AlbumList : LeafScreen(route = "albumList")


    object ArtistsPage : LeafScreen(route = "artistsPage")
    object ArtistsList : LeafScreen(route = "artistsList")


    object PlaylistsPage : LeafScreen(route = "playlistsPage")
    object MostPlayedList : LeafScreen(route = "mostPlayedList")
    object RecentlyPlayedList : LeafScreen(route = "recentlyPlayedList")
    object RecentlyAddedList : LeafScreen(route = "recentlyAddedList")


    object FavoritePage : LeafScreen(route = "favoritePage")
    object FavoriteList : LeafScreen(route = "favoriteList")

    object SearchPage : LeafScreen(route = "searchPage")
    object AddPlaylist : LeafScreen(route = "addPlaylist")
    object Settings : LeafScreen(route = "settings")
    object Equalizer : LeafScreen(route = "equalizer")
    object Sort : LeafScreen(route = "sort")
    object Shuffle : LeafScreen(route = "dhuffle")
    object Folders : LeafScreen(route = "folders")
    object Rate : LeafScreen(route = "rate")

}