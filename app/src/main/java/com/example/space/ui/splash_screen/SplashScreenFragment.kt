package com.example.space.ui.splash_screen

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.space.R
import com.example.space.databinding.FragmentSplashScreenBinding
import com.example.space.mvi_interfaces.splash_screen.SplashScreenView
import com.example.space.presenters.SplashScreenPresenter
import com.example.space.utils.isOnline
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.concurrent.schedule


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : MvpAppCompatFragment(R.layout.fragment_splash_screen),
    SplashScreenView {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenterProvider: Provider<SplashScreenPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.showAnimation()

    }

    override fun showAnimation() {
        val animation = binding.imageSplashScreenAnim.drawable as AnimatedVectorDrawable
        animation.start()
        Timer().schedule(1500) {
            if (requireContext().isOnline()) {
                presenter.openMainScreen()
            } else {
                presenter.showError()
            }
        }
    }

    override fun showErrorWithInternet() {
        Snackbar.make(requireView(), getString(R.string.no_internet_message), LENGTH_LONG)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}