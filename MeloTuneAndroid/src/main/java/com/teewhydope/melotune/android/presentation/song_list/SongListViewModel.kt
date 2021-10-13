package com.teewhydope.melotune.android.presentation.song_list


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teewhydope.melotune.domain.model.*
import com.teewhydope.melotune.domain.util.GenericMessageInfoQueueUtil
import com.teewhydope.melotune.domain.util.Queue
import com.teewhydope.melotune.interactors.song_list.SearchLocalSongs
import com.teewhydope.melotune.presentation.song_list.LocalSongListEvents
import com.teewhydope.melotune.presentation.song_list.LocalSongListState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SongListViewModel
@Inject
constructor(
    private val searchLocalSongs: SearchLocalSongs,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    val state: MutableState<LocalSongListState> = mutableStateOf(LocalSongListState())

    init {
        loadSongs()
    }

    fun onTriggerEvent(event: LocalSongListEvents) {
        when (event) {
            LocalSongListEvents.LoadSongs -> {
                loadSongs()
            }
            LocalSongListEvents.NewSearch -> {
                newSearch()
            }
            is LocalSongListEvents.OnUpdateQuery -> {
                state.value = state.value.copy(query = event.query)
            }
            is LocalSongListEvents.OnRemoveHeadMessageFromQueue -> {
                removeHeadMessage()
            }
            else -> {
                val messageInfoBuilder = GenericMessageInfo.Builder()
                    .id(UUID.randomUUID().toString())
                    .title("Invalid Event")
                    .uiComponentType(UIComponentType.Dialog)
                    .description("Something went wrong.")
                    .positive(PositiveAction(
                        onPositiveAction = {},
                        positiveBtnTxt = "OK",
                    ))
                    .negative(
                        NegativeAction (
                            onNegativeAction = {},
                        negativeBtnTxt = "CANCEL",
                    )
                    )
                appendToMessageQueue(messageInfo = messageInfoBuilder)
            }
        }
    }

    private fun removeHeadMessage() {
        try {
            val queue = state.value.queue
            queue.remove() // can throw exception if empty
            state.value = state.value.copy(queue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy(queue = queue)
        } catch (e: Exception) {
            //logger.log("Nothing to remove from DialogQueue")
        }
    }


    /**
     * Perform a new search:
     * 2. list position needs to be reset
     */
    private fun newSearch() {
        state.value = state.value.copy(songs = listOf())
        loadSongs()
    }

    private fun loadSongs() {
        searchLocalSongs.execute(
            query = state.value.query
        ).collectCommon(viewModelScope) { dataState ->
            state.value = state.value.copy(isLoading = dataState.isLoading)
            dataState.data?.let { songs ->
                appendSongs(songs)
            }

            dataState.message?.let { message ->
                appendToMessageQueue(message)
            }
        }
    }

    private fun appendSongs(songs: List<Song>) {
        val curr = ArrayList(state.value.songs)
        curr.addAll(songs)
        state.value = state.value.copy(songs = curr)
    }

    private fun appendToMessageQueue(messageInfo: GenericMessageInfo.Builder) {
        if (!GenericMessageInfoQueueUtil()
                .doesMessageAlreadyExistInQueue(
                    queue = state.value.queue,
                    messageInfo = messageInfo.build()
                )
        ) {
            val queue = state.value.queue
            queue.add(messageInfo.build())
            state.value = state.value.copy(queue = queue)
        }
    }

}
