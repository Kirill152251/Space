package com.example.space.mvi_interfaces.splash_screen

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface SplashScreenView: MvpView {
    @AddToEndSingle
    fun showAnimation()

    @OneExecution
    fun showErrorWithInternet()
}