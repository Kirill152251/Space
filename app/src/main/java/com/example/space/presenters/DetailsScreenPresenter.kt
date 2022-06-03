package com.example.space.presenters

import com.example.space.mvp_interfaces.DetailsScreenView
import moxy.MvpPresenter
import javax.inject.Inject


class DetailsScreenPresenter @Inject constructor() : MvpPresenter<DetailsScreenView>() {
    fun showUi(url: String) {
        viewState.showPhoto(url)
    }

    fun showOnboardingScreen(){
        viewState.showOnboardingScreen()
    }
}