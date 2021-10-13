package com.teewhydope.melotune.android

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewConfiguration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.teewhydope.melotune.android.presentation.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalFoundationApi
@InternalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var imageLoader: ImageLoader

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hasSoftKey: Boolean = ViewConfiguration.get(this).hasPermanentMenuKey()
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        requestPermission(hasSoftKey = hasSoftKey)
    }


    @ExperimentalAnimationApi
    @InternalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermission(hasSoftKey: Boolean) {
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    if (it.value) { // Do something if permission granted
                        Log.d("LOG_TAG", "permission granted by the user")
                        setContent {
                            MaterialTheme() {
                                ProvideWindowInsets {
                                    Nav(hasSoftKey = hasSoftKey)
                                }
                            }
                        }

                    } else { // Do something as the permission is not granted
                        Log.d("LOG_TAG", "permission denied by the user")
                    }
                }
            }

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

    }


    @Composable
    fun Nav(
        hasSoftKey: Boolean
    ) {

        BoxWithConstraints {
            MaterialTheme() {
                val navController = rememberAnimatedNavController()
                Navigation(
                    imageLoader = imageLoader,
                    hasSoftKey = hasSoftKey,
                    width = constraints.maxWidth / 2,
                    navController = navController,
                )
            }
        }
    }

    @Composable
    fun HomeAppBar(
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        TopAppBar(
            title = {
                Row {
                    Image(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = null
                    )
                    Icon(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .heightIn(max = 24.dp)
                    )
                }
            },
            backgroundColor = backgroundColor,
            actions = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    IconButton(
                        onClick = { /* TODO: Open search */ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { /* TODO: Open account? */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null
                        )
                    }
                }
            },
            modifier = modifier
        )
    }
}



