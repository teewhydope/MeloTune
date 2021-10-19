package com.teewhydope.melotune.android.presentation.home

import HomeTab
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.teewhydope.melotune.android.util.Constants.tabCurrentStatus
import com.teewhydope.melotune.presentation.song_list.LocalSongListEvents
import com.teewhydope.melotune.presentation.song_list.LocalSongListState

@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
fun AppHome(
    navController: NavHostController,
    state: LocalSongListState,
    onTriggerEvent: (LocalSongListEvents) -> Unit,
    imageLoader: ImageLoader,
) {
    val context = LocalContext.current
    val menuExpanded = remember { mutableStateOf(false) }
    val tabStatus = tabCurrentStatus.observeAsState()
    val topBar: @Composable () -> Unit = {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    text = "MeloTune",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },

            actions = {
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Clicked Search", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        menuExpanded.value = true
                    }
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .width(200.dp)
                            .wrapContentSize(Alignment.TopStart),
                        expanded = menuExpanded.value,
                        onDismissRequest = {
                            menuExpanded.value = false
                        }
                    ) {
                        when (tabStatus.value) {
                            0, 1, 2, 3, 4 -> {
                                DropdownMenuItem(onClick = { /*Handle Shuffle All*/ }) {
                                    Text(text = "Shuffle All")
                                }
                                if(tabStatus.value == 1){
                                    DropdownMenuItem(onClick = { /*Handle Sort by*/ }) {
                                        Text(text = "View as")
                                    }
                                }
                                DropdownMenuItem(onClick = { /*Handle Sort by*/ }) {
                                    Text(text = "Sort by")
                                }
                                DropdownMenuItem(onClick = { /*Handle Equalizer*/ }) {
                                    Text(text = "Equalizer")
                                }
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Settings")
                                }
                                DropdownMenuItem(onClick = { /*Handle About*/ }) {
                                    Text(text = "About")
                                }

                            }

                            2 -> {
                                DropdownMenuItem(onClick = { /*Handle Clear call log*/ }) {
                                    Text(text = "Clear call log")
                                }
                                DropdownMenuItem(onClick = { /*Handle Settings*/ }) {
                                    Text(text = "Settings")
                                }
                            }
                        }
                    }
                }

            },
            //backgroundColor = WhatsAppThemeColor,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }
    Scaffold(
        topBar = {
            topBar()
        },
        content = {
            HomeTab(
                navController,
                state = state,
                onTriggerEvent = onTriggerEvent,
                imageLoader = imageLoader,
            )
        },
    )
}