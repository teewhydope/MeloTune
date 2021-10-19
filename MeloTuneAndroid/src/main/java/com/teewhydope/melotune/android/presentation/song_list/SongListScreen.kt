package com.teewhydope.melotune.android.presentation.song_list

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.teewhydope.melotune.android.R
import com.teewhydope.melotune.android.presentation.components.simpleVerticalScrollbar
import com.teewhydope.melotune.android.presentation.theme.MeloTuneAppTheme
import com.teewhydope.melotune.presentation.song_list.LocalSongListEvents
import com.teewhydope.melotune.presentation.song_list.LocalSongListState

@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun SongListScreen(
    state: LocalSongListState,
    onTriggerEvent: (LocalSongListEvents) -> Unit,
    imageLoader: ImageLoader,
) {

    val context = LocalContext.current
    //val lifecycle = LocalLifecycleOwner.current.lifecycle
    val listState = rememberLazyListState()
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    var currIndex by remember { mutableStateOf(0) }
    val song = state.songs.sortedBy { it.displayName }
    val menuExpanded = remember { mutableStateOf(false) }



    DisposableEffect(key1 = exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    MeloTuneAppTheme(
        dialogQueue = state.queue,
        onRemoveHeadMessageFromQueue = {
            onTriggerEvent(LocalSongListEvents.OnRemoveHeadMessageFromQueue)
        }
    ) {
        if (state.isLoading && song.isEmpty()) {
            Surface(
                Modifier.fillMaxSize()
            ) {
                Text(
                    "Fetching Audio Files...",
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
            }
        } else if (song.isEmpty()) {
            //NothingHere()
            println("Nothing Here")
        } else {
            Box(
                Modifier
                    .fillMaxHeight(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .simpleVerticalScrollbar(listState),
                    state = listState,
                ) {
                    item {
                        Spacer(Modifier.height(5.dp))
                    }
                    item {
                        Row(
                            verticalAlignment =  Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Play All",
                                modifier = Modifier
                                    .padding(6.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "(${song.size} Songs)",
                                modifier = Modifier.padding(6.dp),
                                color = Color.DarkGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    itemsIndexed(song) { index, _ ->
                        Divider(color = Color.Black.copy(alpha = 0.2f))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currIndex = index
                                    exoPlayer.apply {
                                        val dataSourceFactory: DataSource.Factory =
                                            DefaultDataSourceFactory(
                                                context,
                                                Util.getUserAgent(context, context.packageName)
                                            )
                                        val source =
                                            ProgressiveMediaSource
                                                .Factory(dataSourceFactory)
                                                .createMediaSource(
                                                    MediaItem
                                                        .Builder()
                                                        .setUri(
                                                            Uri.parse(song[index].uri.toString())
                                                        )
                                                        .build()
                                                )
                                        this.repeatMode = Player.REPEAT_MODE_ALL
                                        this.setMediaSource(source)
                                        prepare()
                                        play()
                                    }
                                }
                                .padding(vertical = 5.dp)
                        ) {
                            Spacer(Modifier.width(5.dp))
                            Image(
                                painter = rememberImagePainter(
                                    imageLoader = imageLoader,
                                    data = song[index].albumArt,
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    }
                                ),
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(5.dp))
                            Column(
                                Modifier
                                    .fillMaxWidth(0.85f)
                            ) {
                                Text(
                                    song[index].displayName.dropLast(4),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                song[index].artist?.let {
                                    Text(
                                        it,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            IconButton(
                                modifier = Modifier.wrapContentWidth(
                                    align = Alignment.End,
                                ),
                                onClick = {
                                    menuExpanded.value = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.MoreVert,
                                    contentDescription = "Menu",
                                    tint = Color.DarkGray
                                )
                            }
                        }
                    }
                    item {
                        Column {
                            Divider(color = Color.Black.copy(alpha = 0.2f))
                            Spacer(Modifier.height(100.dp))
                        }
                    }
                }
                Card(
                    elevation = 20.dp,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .align(Alignment.BottomCenter),
                ) {
                    Row(
                        //Modifier.background(Color.DarkGray.copy(alpha = 0.2f)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        song[currIndex].albumArt?.let {
                            Image(
                                painter = rememberImagePainter(
                                    imageLoader = imageLoader,
                                    data = it,
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    }
                                ),
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp),
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        ) {
                            Text(
                                song[currIndex].displayName.dropLast(4),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            song[currIndex].artist?.let {
                                Text(
                                    it,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        var isPlaying by remember { mutableStateOf(false) }
                        Icon(
                            painter = painterResource(id = if (isPlaying) R.drawable.play_arrow else R.drawable.pause),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    isPlaying = !isPlaying
                                    if (isPlaying) {
                                        exoPlayer
                                            .apply {
                                                val mediaItem: MediaItem =
                                                    MediaItem.fromUri(song[currIndex].uri.toString())
                                                addMediaItem(mediaItem)
                                                prepare()
                                                playWhenReady = false
                                                repeatMode = Player.REPEAT_MODE_ALL
                                            }
                                    } else {
                                        exoPlayer.apply {
                                            val mediaItem: MediaItem =
                                                MediaItem.fromUri(song[currIndex].uri.toString())
                                            addMediaItem(mediaItem)
                                            prepare()
                                            playWhenReady = true
                                            repeatMode = Player.REPEAT_MODE_ALL
                                        }
                                    }

                                }
                                .fillMaxSize()
                        )
                    }
                }

            }
        }
    }
}