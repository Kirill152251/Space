package com.example.space

import android.app.Application
import android.util.Log
import com.github.terrakok.cicerone.Cicerone
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {}