package com.example.space.presenters

import com.example.space.Screens.Main
import com.example.space.mvi_interfaces.splash_screen.SplashScreenView
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject
import javax.inject.Singleton


class SplashScreenPresenter @Inject constructor(private val router: Router) :
    MvpPresenter<SplashScreenView>() {
    fun showAnimation() {
        viewState.showAnimation()
    }

    fun showError() {
        viewState.showErrorWithInternet()
    }

    fun openMainScreen() {
        router.navigateTo(Main())
    }
}