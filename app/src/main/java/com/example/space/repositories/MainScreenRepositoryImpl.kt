package com.example.space.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.api_response_model.Photo
import com.example.space.repositories.interfaces.MainScreenRepository
import com.example.space.ui.main_screen.PhotoPagingSource
import com.example.space.utils.NETWORK_PAGE_SIZE
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject


@FragmentScoped
class MainScreenRepositoryImpl @Inject constructor(private val pagingSource: PhotoPagingSource) :
    MainScreenRepository {
    override fun getPhotos(): Flowable<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pagingSource }
        ).flowable
    }

}