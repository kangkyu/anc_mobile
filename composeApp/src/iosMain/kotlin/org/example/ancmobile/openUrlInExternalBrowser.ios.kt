package org.example.ancmobile

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrlInExternalBrowser(url: String) {
    NSURL.URLWithString(url)?.let { uri ->
        UIApplication.sharedApplication.canOpenURL(uri).also { canOpen ->
            if (canOpen) {
                UIApplication.sharedApplication.openURL(uri)
            }
        }
    }
}
