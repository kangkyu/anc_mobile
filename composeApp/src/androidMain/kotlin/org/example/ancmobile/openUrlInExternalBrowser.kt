package org.example.ancmobile

actual fun openUrlInExternalBrowser(url: String) {
    MainActivity.instance?.openBrowser(url)
}
