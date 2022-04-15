package com.example.space.di

import com.example.space.repositories.MainScreenRepositoryImpl
import com.example.space.repositories.MapScreenRepositoryImpl
import com.example.space.repositories.interfaces.MainScreenRepository
import com.example.space.repositories.interfaces.MapScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMainScreenRepository(repositoryImpl: MainScreenRepositoryImpl) : MainScreenRepository

    @Binds
    fun bindMapScreenRepository(repositoryImpl: MapScreenRepositoryImpl) : MapScreenRepository
}