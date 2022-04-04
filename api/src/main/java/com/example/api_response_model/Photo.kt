package com.example.api_response_model

import com.squareup.moshi.Json


data class Photo(
    val camera: Camera,
    @field:Json(name = "img_src")
    val image: String,
    val rover: Rover
)