package com.example.space.ui.main_screen

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.NasaApiService
import com.example.api_response_model.NasaApiResponse
import com.example.api_response_model.Photo
import com.example.space.utils.STARTING_PAGE_INDEX
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoPagingSource @Inject constructor(
    private val apiService: NasaApiService
) : RxPagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return apiService.getPhotos(pageIndex)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, pageIndex, params.loadSize) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: NasaApiResponse, pageIndex: Int, loadSize: Int): LoadResult<Int, Photo> {
        return LoadResult.Page(
            data = data.photos,
            prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
            nextKey = if (data.photos.size == loadSize) pageIndex + 1 else null
        )
    }

}