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
import com.mmk.kmpnotifier.notification.NotifierManager
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
                })
            }
            App()
        }
        permissionUtil.askNotificationPermission {
            println("HasNotification Permission: $it")
        }
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
