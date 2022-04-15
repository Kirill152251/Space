package com.example.space.repositories.interfaces

import com.example.space.ui.map_screen.MapMarker

interface MapScreenRepository {
    fun getMarkers(): List<MapMarker>
    fun saveMarker(marker: MapMarker)
    fun deleteMarker(position: Int)
    fun clearRepository()
}