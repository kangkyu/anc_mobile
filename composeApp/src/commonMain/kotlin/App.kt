import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.crossfade
import ui.ChurchInfoView
import ui.JuboView
import ui.SermonVideosView

@Composable
@coil3.annotation.ExperimentalCoilApi
fun App() {
    MaterialTheme {
        MainTabView()
    }

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }
            .crossfade(true)
            .build()
    }
}

@Composable
fun MainTabView() {
    val items = MainTabItems.entries.toList()
    val selectedTab = remember { mutableStateOf(MainTabItems.Info) }

    Scaffold(
        bottomBar = {
            TabRow(selectedTabIndex = selectedTab.value.ordinal) {
                items.forEach { tabItem ->
                    Tab(
                        text = { Text(tabItem.title, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)) },
                        icon = { Icon(tabItem.icon, contentDescription = null) },
                        selected = selectedTab.value == tabItem,
                        onClick = { selectedTab.value = tabItem }
                    )
                }
            }
        }
    ) {
        when (selectedTab.value) {
            MainTabItems.Jubo -> JuboView()
            MainTabItems.Preach -> SermonVideosView()
            MainTabItems.Info -> ChurchInfoView()
        }
    }
}

enum class MainTabItems(val title: String, val icon: ImageVector = Icons.Default.Info) {
    Preach("Sermons", Icons.Default.Person),
    Jubo("Jubo", Icons.Default.Done),
    Info("Info", Icons.Default.Info)
}
