package com.example.space.utils

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RetainedState(private val state: SavedStateHandle) : ViewModel() {

    fun <T> get(key: String): T? {
        return state.get(key)
    }

    fun <T> remove(key: String): T? {
        return state.remove(key)
    }

    fun <T> set(key: String, value: T) {
        state.set(key, value)
    }

}