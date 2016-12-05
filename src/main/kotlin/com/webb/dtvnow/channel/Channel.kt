package com.webb.dtvnow.channel

import com.fasterxml.jackson.annotation.JsonProperty

data class Channel(@JsonProperty("name") val name: String,
                   @JsonProperty("url") val url: String,
                   @JsonProperty("present") val present: Boolean) {
}
