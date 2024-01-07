package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
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
import androidx.compose.ui.platform.LocalUriHandler
import api.ChruchAPI
import api.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import model.ExternalURL
import org.jetbrains.compose.resources.LoadState

@Composable
fun JuboView() {
    val externalLink = remember { mutableStateOf(ExternalURL("")) }
    var loadState by remember { mutableStateOf(LoadingState.Loading) }

    LaunchedEffect(Unit) {
        try {
            ChruchAPI.shared.getJuboExternalURL().onSuccess {
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
            JuboSuccessView(externalLink = externalLink)
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
fun JuboSuccessView(externalLink: MutableState<ExternalURL>) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            uriHandler.openUri(externalLink.value.url)
        }) {
            Text("주보 보기")
        }
    }
}

@Composable
fun JuboLoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Loading...")
    }
}
