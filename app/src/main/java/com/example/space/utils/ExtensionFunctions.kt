package com.example.space.utils

fun String.parseImageUrl(): String {
    val new = this.removeRange(0..3)
    return "https$new"
}