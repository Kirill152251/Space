package com.example.space

import android.content.Intent
import android.os.Bundle
import com.example.space.ui.details_screen.DetailsScreenFragment
import com.example.space.ui.main_screen.MainScreenFragment
import com.example.space.ui.map_screen.MapScreenFragment
import com.example.space.ui.splash_screen.SplashScreenFragment
import com.example.space.utils.URL_KEY
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Splash() = FragmentScreen { SplashScreenFragment() }
    fun Main() = FragmentScreen { MainScreenFragment() }
    fun DetailsScreen(photoUrl: String) = FragmentScreen {
        DetailsScreenFragment.newInstance(photoUrl)
    }
    fun MapScreen() = FragmentScreen { MapScreenFragment() }
}