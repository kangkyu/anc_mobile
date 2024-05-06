package org.example.ancmobile

import java.awt.Desktop
import java.net.URI

actual fun openUrlInExternalBrowser(url: String) {
    // TODO: implement better desktop actual
    val desktop = Desktop.getDesktop()
    desktop.browse(URI.create(url))
}
