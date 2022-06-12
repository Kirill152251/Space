package com.example.space.presenters

import android.graphics.Bitmap
import android.util.Log
import com.example.space.mvp_interfaces.DetailsScreenView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class DetailsScreenPresenter @Inject constructor() : MvpPresenter<DetailsScreenView>() {
    fun showUi(url: String) {
        viewState.showPhoto(url)
    }

    fun showOnboardingScreen() {
        viewState.showOnboardingScreen()
    }

    fun compressBitmap(
        imageFolder: File,
        bitmap: Bitmap,
        file: File,
        coroutineScope: CoroutineScope
    ) {
        val stream = FileOutputStream(file)
        coroutineScope.launch {
            imageFolder.mkdirs()
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, stream)
            stream.flush()
            stream.close()
        }
    }
}