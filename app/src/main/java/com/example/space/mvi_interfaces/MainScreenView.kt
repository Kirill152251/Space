package com.example.space.mvi_interfaces

import androidx.paging.PagingData
import com.example.api_response_model.Photo
import com.example.space.ui.main_screen.MainScreenAdapter
import io.reactivex.rxjava3.core.Flowable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainScreenView: MvpView {
    @AddToEndSingle
    fun showRecycleView(photos: Flowable<PagingData<Photo>>, adapter: MainScreenAdapter)
}