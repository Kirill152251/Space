package com.example.space.mvp_interfaces

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface SplashScreenView: MvpView {
    @AddToEndSingle
    fun showAnimation()

    @OneExecution
    fun showErrorWithInternet()
}