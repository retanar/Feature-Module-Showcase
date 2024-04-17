package com.featuremodule.data.floof

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoxResponse(
    val image: String? = null,
    val link: String? = null,
)
