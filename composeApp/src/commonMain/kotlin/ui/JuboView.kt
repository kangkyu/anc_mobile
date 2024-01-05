package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.ChruchAPI
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import model.ExternalURL

@Composable
fun JuboView() {
    var viewLink by remember { mutableStateOf(false) }
    val externalLink = remember { mutableStateOf(ExternalURL("")) }

    LaunchedEffect(Unit) {
        try {
            ChruchAPI.shared.getJuboExternalURL().onSuccess {
                println(it)
                try {
                    externalLink.value = Json.decodeFromString<ExternalURL>(it)
                } catch (e: Exception) {
                    println(e)
                }

            }.onFailure {
                println(it)
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewLink = !viewLink }) {
            if (viewLink) Text("Hide") else Text("Show")
        }
        if (viewLink) {
            Text(externalLink.value.url)
        }
    }
}
