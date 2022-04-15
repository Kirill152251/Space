package com.example.space.repositories

import com.example.space.repositories.interfaces.MapScreenRepository
import com.example.space.ui.map_screen.MapMarker
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MapScreenRepositoryImpl @Inject constructor() : MapScreenRepository {

    private val markersList = mutableListOf<MapMarker>()

    override fun getMarkers(): List<MapMarker> {
        return markersList
    }

    override fun saveMarker(marker: MapMarker) {
        markersList.add(marker)
    }

    override fun deleteMarker(position: Int) {
        markersList.removeAt(position)
    }

    override fun clearRepository() {
        markersList.clear()
    }
}