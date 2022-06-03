package com.example.space.presenters

import android.view.View
import com.example.space.mvp_interfaces.MapScreenView
import com.example.space.repositories.interfaces.MapScreenRepository
import com.example.space.ui.map_screen.MapMarker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpPresenter
import javax.inject.Inject

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

    fun deleteMarkerFromCache(position: Int) {
        repository.deleteMarker(position)
    }

    fun submitMarkerList(newList: List<MapMarker>) {
        viewState.submitMarkerList(newList)
    }
}