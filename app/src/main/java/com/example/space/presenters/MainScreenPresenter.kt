package com.example.space.presenters

import androidx.paging.PagingData
import com.example.api_response_model.Photo
import com.example.space.mvi_interfaces.main_screen.MainScreenView
import com.example.space.repositories.interfaces.MainScreenRepository
import com.example.space.ui.main_screen.MainScreenAdapter
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Flowable
import moxy.MvpPresenter
import javax.inject.Inject

class MainScreenPresenter @Inject constructor(
    private val repository: MainScreenRepository,
    private val router: Router
) : MvpPresenter<MainScreenView>() {

    fun showUi(adapter: MainScreenAdapter) {
        val photos = fetchPhotos()
        viewState.showRecycleView(photos, adapter)
    }

    private fun fetchPhotos(): Flowable<PagingData<Photo>> = repository.getPhotos()
}