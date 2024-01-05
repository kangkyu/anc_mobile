package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalURL(
    @SerialName("external_url")
    val url: String
)
