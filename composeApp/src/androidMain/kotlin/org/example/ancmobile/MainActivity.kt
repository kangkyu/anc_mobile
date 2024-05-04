package org.example.ancmobile

import App
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.permission.permissionUtil
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

class MainActivity : ComponentActivity() {

    private val permissionUtil by permissionUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
            )
        )
        NotifierManager.onCreateOrOnNewIntent(intent)

        setContent {
            LaunchedEffect(Unit) {
                println("**** LaunchedEffectApp is called")

                NotifierManager.addListener(object : NotifierManager.Listener {
                    override fun onPushNotification(title: String?, body: String?) {
                        println("**** $title $body")
                    }
                    override fun onNewToken(token: String) {
                        println("**** onNewToken: $token")
                    }
                    override fun onNotificationClicked(data: PayloadData) {
                        println("Notification clicked, Notification payloadData: $data")
                    }
                })
            }
            App()
        }
        permissionUtil.askNotificationPermission {
            println("HasNotification Permission: $it")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

    fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    companion object {
        var instance: MainActivity? = null
            private set
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
