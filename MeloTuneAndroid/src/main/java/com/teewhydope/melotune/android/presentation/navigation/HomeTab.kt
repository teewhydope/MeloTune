import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.teewhydope.melotune.android.presentation.album.AlbumListScreen
import com.teewhydope.melotune.android.presentation.artist.ArtistsListScreen
import com.teewhydope.melotune.android.presentation.favorites.FavoritesListScreen
import com.teewhydope.melotune.android.presentation.playlist.PlayListScreen
import com.teewhydope.melotune.android.presentation.song_list.SongListScreen
import com.teewhydope.melotune.android.util.Constants._tabCurrentStatus
import com.teewhydope.melotune.presentation.song_list.LocalSongListEvents
import com.teewhydope.melotune.presentation.song_list.LocalSongListState
import kotlinx.coroutines.launch


//typealias ComposableFun = @Composable () -> Unit

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun HomeTab(
    navController: NavHostController,
    state: LocalSongListState,
    onTriggerEvent: (LocalSongListEvents) -> Unit,
    imageLoader: ImageLoader,
) {
    val pagerState = rememberPagerState()
    Column {
        Tab(pagerState)
        TabsContent(
            pagerState,
            navController,
            state = state,
            onTriggerEvent = onTriggerEvent,
            imageLoader = imageLoader,
        )
    }
}

@ExperimentalPagerApi
@Composable
fun Tab(pagerState: PagerState) {
    val list = listOf("Songs", "Albums", "Artists", "Playlists", "Favorites")
    val scope = rememberCoroutineScope()
    _tabCurrentStatus.value = pagerState.currentPage

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        //backgroundColor = WhatsAppThemeColor,
        contentColor = Color.Gray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 3.dp,
                color = Color.White
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = Color.White
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    navController: NavHostController,
    state: LocalSongListState,
    onTriggerEvent: (LocalSongListEvents) -> Unit,
    imageLoader: ImageLoader,
) {
    HorizontalPager(state = pagerState, count = 5) { page ->
        when (page) {
            0 -> SongListScreen(
                state = state,
                onTriggerEvent = onTriggerEvent,
                imageLoader = imageLoader,
            )
            1 -> AlbumListScreen()
            2 -> ArtistsListScreen()
            3 -> PlayListScreen()
            4 -> FavoritesListScreen()

        }
    }
}