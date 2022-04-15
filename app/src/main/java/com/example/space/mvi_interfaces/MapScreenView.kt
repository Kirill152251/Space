package com.example.space.mvi_interfaces

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip


interface MapScreenView: MvpView {
    @Skip
    fun showMapTypeDialog(mapType: Int)

    @AddToEndSingle
    fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>)
}