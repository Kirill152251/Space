package com.example.space.presenters

import android.content.SharedPreferences
import com.example.space.mvi_interfaces.MapScreenView
import moxy.MvpPresenter
import javax.inject.Inject


class MapScreenPresenter @Inject constructor() : MvpPresenter<MapScreenView>() {
    fun showMapTypeDialog(mapType: Int) {
        viewState.showMapTypeDialog(mapType)
    }
}