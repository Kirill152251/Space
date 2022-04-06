package com.example

import com.example.api_response_model.NasaApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&page=2&api_key=DEMO_KEY
interface NasaApiService {
    @GET("photos")
    fun getPhotos(
        @Query("page") page: Int
    ): Single<NasaApiResponse>
}