package com.example.space.di

import com.example.space.repositories.MainScreenRepositoryImpl
import com.example.space.repositories.interfaces.MainScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMainScreenRepository(repositoryImpl: MainScreenRepositoryImpl) : MainScreenRepository
}