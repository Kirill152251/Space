package com.example.space.presenters

import com.example.space.Screens.Main
import com.example.space.Screens.MapScreen
import com.example.space.Screens.Splash
import com.example.space.mvi_interfaces.MainActivityView
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject


class MainActivityPresenter @Inject constructor(
    private val router: Router
): MvpPresenter<MainActivityView>() {

    fun openSplashScreen() {
        router.navigateTo(Splash())
    }

    fun navigateToMainScreen() {
        router.navigateTo(Main())
    }

    fun navigateToMapScreen() {
        router.navigateTo( MapScreen())
    }
}