package com.example.api_response_model

import com.squareup.moshi.Json


data class Camera(
    @field:Json(name = "full_name")
    val fullName: String
)