package com.teewhydope.melotune.android.presentation.song_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
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
import com.teewhydope.melotune.android.R
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
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val listState = rememberLazyListState()
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    var currIndex by remember { mutableStateOf(0) }
    val song = state.songs.sortedBy { it.displayName }


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
                Column(
                ) {
                    /* AndroidView(
                     modifier = Modifier
                         .height(300.dp)
                         .fillMaxWidth(),
                     factory = { playerView }
                 )*/

                    LazyColumn(
                        state = listState
                    ) {
                        item {
                            Spacer(Modifier.height(5.dp))
                        }
                        item {
                            Surface(
                                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                color = Color.DarkGray
                            ) {
                                Text(
                                    text = "${song.size} Songs",
                                    modifier = Modifier.padding(10.dp),
                                    color = Color.White
                                )
                            }
                        }
                        itemsIndexed(song) { index, _ -> Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        exoPlayer.pause()
                                        currIndex = index
                                        exoPlayer.apply {
                                            val mediaItem: MediaItem =
                                                MediaItem.fromUri(song[currIndex].uri)
                                            addMediaItem(mediaItem)
                                            prepare()
                                            playWhenReady = true
                                        }
                                    }
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
                                Column {
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
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .align(Alignment.BottomCenter),
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