package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import androidx.compose.foundation.lazy.items
import coil3.compose.AsyncImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.IO
import model.SearchListResponse
import model.SearchResult
import org.example.project.openUrlInExternalBrowser

@Composable
fun SermonVideosView() {
    var outcome: String? by remember { mutableStateOf(null) }
    var response: SearchListResponse? by remember { mutableStateOf(null) }
    val myApiKey = "YOUR_YOUTUBE_API_KEY" // TODO: use config variable
    val channelId = "UCIsWNZwrpO_CnlaXO5Oc6bQ"
    // val anconnuri = "https://www.youtube.com/channel/UCIsWNZwrpO_CnlaXO5Oc6bQ"
    // val passionworship = "https://www.youtube.com/channel/UCBTZoebaG4rvChzKQ2D80-w"

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val client = HttpClient()

            val res = client.get("https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=$channelId&maxResults=7&order=date&type=video&key=$myApiKey")
            client.close()
            outcome = res.body()
        }

        response = if (outcome != null) {
            val jsonString = outcome.toString()
            val parsedResponse = withContext(Dispatchers.Default) {
                Json.decodeFromString<SearchListResponse>(jsonString)
            }
            parsedResponse
        } else {
            // TODO: Handle the case when json response is empty
            null
        }
    }

    val videos = response?.items?.toVideos() ?: emptyList()
    VideosGrid(videos = videos, clickFunc = { openUrlInExternalBrowser("https://youtu.be/${it.videoId}") })
}

fun List<SearchResult>.toVideos(): List<YouTubeVideo> {
    return this.map {
        YouTubeVideo(it.id.videoId, it.snippet.title, it.snippet.thumbnails.high.url)
    }
}

@Composable
fun VideosGrid(videos: List<YouTubeVideo>, clickFunc: (YouTubeVideo) -> Unit) {

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(
            items = videos,
            itemContent = { video: YouTubeVideo ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                // start VideoActivity and pass the video details
                                clickFunc(video)
                            },
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = video.thumbnailUrl,
                            contentDescription = video.title,
                            modifier = Modifier.height(99.dp).width(176.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            text = video.title,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                    )
                }
            }
        )
    }
}

data class YouTubeVideo(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
)
