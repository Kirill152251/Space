package com.example.space.mvi_interfaces

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle


interface MapScreenView: MvpView {
    @AddToEndSingle
    fun showMapTypeDialog(mapType: Int)

    @AddToEndSingle
    fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>)
}