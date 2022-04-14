package com.example.space.presenters

import android.view.View
import com.example.space.mvi_interfaces.MapScreenView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpPresenter
import javax.inject.Inject


class MapScreenPresenter @Inject constructor() : MvpPresenter<MapScreenView>() {

    fun showMapTypeDialog(mapType: Int) {
        viewState.showMapTypeDialog(mapType)
    }

    fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>) {
        viewState.showMarkerManagerBottomSheet(bottomSheetBehavior)
    }

}