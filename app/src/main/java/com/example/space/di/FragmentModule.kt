package com.example.space.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.disposables.CompositeDisposable

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {
    @FragmentScoped
    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
}