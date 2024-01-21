package org.example.project

actual fun openUrlInExternalBrowser(url: String) {
    MainActivity.instance?.openBrowser(url)
}
