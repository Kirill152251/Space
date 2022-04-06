package com.example.space.repositories.interfaces

import androidx.paging.PagingData
import com.example.api_response_model.NasaApiResponse
import com.example.api_response_model.Photo
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MainScreenRepository {
    fun getPhotos(): Flowable<PagingData<Photo>>
}