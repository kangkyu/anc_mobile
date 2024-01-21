package ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import api.ChurchAPI
import api.LoadingState
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import model.ExternalURL

@OptIn(ExperimentalCoilApi::class)
@Composable
fun JuboView() {
    val externalLink = remember { mutableStateOf(ExternalURL("")) }
    var loadState by remember { mutableStateOf(LoadingState.Loading) }

    LaunchedEffect(Dispatchers.IO) {
        try {
            ChurchAPI.shared.getJuboExternalURL().onSuccess {
                externalLink.value = Json.decodeFromString<ExternalURL>(it)
                loadState = LoadingState.Success
            }.onFailure {
                println("******")
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
            JuboImageView(externalLink)
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
fun JuboImageView(externalLink: MutableState<ExternalURL>) {
    val scale = remember { mutableStateOf(1f) }
    val coroutineScope = rememberCoroutineScope()

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .wrapContentSize(align = Alignment.TopStart)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, gestureZoom, _ ->
                    scale.value *= gestureZoom
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        horizontalScrollState.scrollBy(-dragAmount.x)
                        verticalScrollState.scrollBy(-dragAmount.y)
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .verticalScroll(verticalScrollState)
                .fillMaxSize()
                .graphicsLayer(
                    // adding some zoom limits (min 50%, max 200%)
                    scaleX = maxOf(.5f, minOf(3f, scale.value)),
                    scaleY = maxOf(.5f, minOf(3f, scale.value))
                )
        ) {
            JuboImage(externalLink)
        }
    }
}

@Composable
fun JuboImage(externalLink: MutableState<ExternalURL>) {

    SubcomposeAsyncImage(
        model = externalLink.value.url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.TopStart
    ) {
        val state = painter.state
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Text("loading")
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

@Composable
fun JuboLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Loading...")
//        CircularProgressIndicator()
    }
}
