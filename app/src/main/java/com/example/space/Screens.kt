package com.example.space

import com.example.space.ui.main_screen.MainScreenFragment
import com.example.space.ui.splash_screen.SplashScreenFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun Splash() = FragmentScreen { SplashScreenFragment() }
    fun Main() = FragmentScreen { MainScreenFragment() }
}