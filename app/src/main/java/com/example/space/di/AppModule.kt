package com.example.space.di

import android.content.Context
import android.content.SharedPreferences
import com.example.space.utils.MAP_TYPE_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideSharePref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            MAP_TYPE_PREF, Context.MODE_PRIVATE)
    }
}

