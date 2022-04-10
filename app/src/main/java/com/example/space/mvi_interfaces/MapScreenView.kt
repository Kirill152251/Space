package com.example.space.mvi_interfaces

import android.content.SharedPreferences
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle


interface MapScreenView: MvpView {
    @AddToEndSingle
    fun showMapTypeDialog(mapType: Int)
}