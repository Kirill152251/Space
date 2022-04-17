package com.example.space.repositories.interfaces

import androidx.paging.PagingData
import com.example.api_response_model.Photo
import io.reactivex.rxjava3.core.Flowable

interface MainScreenRepository {
    fun getPhotos(): Flowable<PagingData<Photo>>
}