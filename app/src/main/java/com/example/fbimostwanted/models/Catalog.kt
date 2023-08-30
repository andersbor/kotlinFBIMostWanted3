package com.example.fbimostwanted.models

import com.squareup.moshi.Json

data class Catalog(
    @field:Json(name = "items") val items: List<Item>,
    val page: Int,
    val total: Int
)