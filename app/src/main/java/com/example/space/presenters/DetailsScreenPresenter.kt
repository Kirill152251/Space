package com.example.space.presenters

import com.example.space.mvi_interfaces.details_screen.DetailsScreenView
import dagger.hilt.android.scopes.FragmentScoped
import moxy.MvpPresenter
import moxy.presenter.InjectPresenter
import javax.inject.Inject
import javax.inject.Singleton



class DetailsScreenPresenter @Inject constructor() : MvpPresenter<DetailsScreenView>() {
    fun showUi(url: String) {
        viewState.showPhoto(url)
    }
}