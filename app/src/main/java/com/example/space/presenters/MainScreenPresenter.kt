package com.example.space.presenters

import androidx.paging.PagingData
import com.example.api_response_model.Photo
import com.example.space.Screens.DetailsScreen
import com.example.space.mvp_interfaces.MainScreenView
import com.example.space.repositories.interfaces.MainScreenRepository
import com.example.space.ui.main_screen.MainScreenAdapter
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.disposables.ArrayCompositeDisposable
import moxy.MvpPresenter
import javax.inject.Inject

class MainScreenPresenter @Inject constructor(
    private val repository: MainScreenRepository,
    private val compositeDisposable: CompositeDisposable,
    private val router: Router
) : MvpPresenter<MainScreenView>() {

    fun showUi(adapter: MainScreenAdapter) {
        val photos = fetchPhotos()
        compositeDisposable.add(photos.subscribe {
            viewState.showRecycleView(it, adapter)
        })
    }

    private fun fetchPhotos(): Flowable<PagingData<Photo>> = repository.getPhotos()

    fun openDetailsScreen(url: String) {
        router.navigateTo(DetailsScreen(url))
    }
}