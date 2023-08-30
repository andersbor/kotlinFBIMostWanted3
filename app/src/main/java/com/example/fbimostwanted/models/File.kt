package com.example.fbimostwanted.models

import com.squareup.moshi.Json

data class File(
    @field:Json(name = "name") val name: String,
    val url: String
)