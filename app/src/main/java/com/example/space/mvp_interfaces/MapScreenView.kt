package com.example.space.mvp_interfaces

import android.view.View
import com.example.space.ui.map_screen.MapMarker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip


interface MapScreenView: MvpView {
    @Skip
    fun showMapTypeDialog(mapType: Int)

    @AddToEndSingle
    fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>)

    @AddToEndSingle
    fun submitMarkerList(newList: List<MapMarker>)
}