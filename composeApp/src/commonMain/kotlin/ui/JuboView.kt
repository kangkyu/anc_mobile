package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import api.ChurchAPI
import api.LoadingState
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.setSingletonImageLoaderFactory
import coil3.fetch.NetworkFetcher
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.serialization.json.Json
import model.ExternalURL

@OptIn(ExperimentalCoilApi::class)
@Composable
fun JuboView() {
    setSingletonImageLoaderFactory { context ->
        newImageLoader(context, false)
    }

    val externalLink = remember { mutableStateOf(ExternalURL("")) }
    var loadState by remember { mutableStateOf(LoadingState.Loading) }

    LaunchedEffect(Unit) {
        try {
            ChurchAPI.shared.getJuboExternalURL().onSuccess {
                externalLink.value = Json.decodeFromString<ExternalURL>(it)
                loadState = LoadingState.Success
            }.onFailure {
                println(it)
                loadState = LoadingState.Failure
            }
        } catch (e: Exception) {
            println(e)
            loadState = LoadingState.Error
        }
    }

    when (loadState) {
        LoadingState.Loading -> {
            JuboLoadingView()
        }

        LoadingState.Success -> {
            JuboImageView(externalLink, onLoading = {
                loadState = LoadingState.Loading
            })
        }

        LoadingState.Failure -> {
            Text("Failure")
        }

        LoadingState.Error -> {
            Text("Error")
        }
    }
}

@Composable
fun JuboImageView(externalLink: MutableState<ExternalURL>, onLoading: () -> Unit = {}) {
    val verticalScrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .verticalScroll(verticalScrollState)
            .size(1080.dp),
        contentAlignment = Alignment.Center
    ) {
        val horizontalScrollState = rememberScrollState()
        SubcomposeAsyncImage(
            model = externalLink.value.url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .fillMaxSize()
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Error -> {
                    Text("error")
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}

@Composable
fun JuboLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Loading...")
    }
}

@OptIn(ExperimentalCoilApi::class)
fun newImageLoader(
    context: PlatformContext,
    debug: Boolean = false,
): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            add(NetworkFetcher.Factory())
            // SVGs
//            add(SvgDecoder.Factory())
//            addPlatformComponents()
        }
        .memoryCache {
            MemoryCache.Builder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(context, percent = 0.25)
                .build()
        }
        .diskCache {
//            newDiskCache()
            null
        }
        // Show a short crossfade when loading images asynchronously.
        .crossfade(true)
        // Enable logging if this is a debug build.
//        .apply {
//            if (debug) {
//                logger(DebugLogger())
//            }
//        }
        .build()
}
