package com.example.space.presenters

import com.example.space.Screens.Splash
import com.example.space.mvi_interfaces.MainActivityView
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject
import javax.inject.Singleton


class MainActivityPresenter @Inject constructor(
    private val router: Router
): MvpPresenter<MainActivityView>() {

    fun openSplashScreen() {
        router.navigateTo(Splash())
    }
}