package com.example.space.mvi_interfaces

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface DetailsScreenView: MvpView {
    @AddToEndSingle
    fun showPhoto(url: String)

    @AddToEndSingle
    fun showOnboardingScreen()
}