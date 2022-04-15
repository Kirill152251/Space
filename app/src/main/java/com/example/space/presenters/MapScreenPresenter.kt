package com.example.space.presenters

import android.view.View
import com.example.space.mvi_interfaces.MapScreenView
import com.example.space.repositories.interfaces.MapScreenRepository
import com.example.space.ui.map_screen.MapMarker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpPresenter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapScreenPresenter @Inject constructor(private val repository: MapScreenRepository) :
    MvpPresenter<MapScreenView>() {

    fun showMapTypeDialog(mapType: Int) {
        viewState.showMapTypeDialog(mapType)
    }

    fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>) {
        viewState.showMarkerManagerBottomSheet(bottomSheetBehavior)
    }

    fun getMarkers() = repository.getMarkers()

    fun saveMarker(marker: MapMarker) {
        repository.saveMarker(marker)
    }

    fun deleteMarker(position: Int) {
        repository.deleteMarker(position)
    }

    fun clearRepository() = repository.clearRepository()
}