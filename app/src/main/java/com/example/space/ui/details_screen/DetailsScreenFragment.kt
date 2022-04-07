package com.example.space.ui.details_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.space.R
import com.example.space.databinding.FragmentDetailsScreenBinding
import com.example.space.mvi_interfaces.details_screen.DetailsScreenView
import com.example.space.presenters.DetailsScreenPresenter
import com.example.space.utils.URL_KEY
import com.example.space.utils.parseImageUrl
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.concurrent.schedule


@AndroidEntryPoint
class DetailsScreenFragment() : MvpAppCompatFragment(R.layout.fragment_details_screen), DetailsScreenView {

    private var _binding: FragmentDetailsScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenterProvider: Provider<DetailsScreenPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        val url = args?.getString(URL_KEY)?.parseImageUrl()
        binding.imageBackToMain.setOnClickListener {
            requireActivity().onBackPressed()
        }
        presenter.showUi(url!!)
        presenter.showOnboardingScreen()
    }

    override fun showPhoto(url: String) {
        binding.imageDetailPhoto.load(url) {
            crossfade(true)
            crossfade(300)
        }
    }

    override fun showOnboardingScreen() {
        val settings = requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
        if (settings.getBoolean("first_launch", true)) {
            binding.apply {
                imageBackToMain.isClickable = false
                imageShare.isClickable = false
                onboardingScreen.visibility = View.VISIBLE
            }
            val hideOnboardingScreen = Runnable {
                if (_binding != null) {
                    binding.apply {
                        imageBackToMain.isClickable = true
                        imageShare.isClickable = true
                        onboardingScreen.visibility = View.GONE
                    }
                }
            }
            binding.onboardingScreen.postDelayed(hideOnboardingScreen, 5000)
            binding.imageOnboardingBackground.setOnClickListener {
                binding.apply {
                    imageBackToMain.isClickable = true
                    imageShare.isClickable = true
                    onboardingScreen.visibility = View.GONE
                }
            }
            settings.edit().putBoolean("first_launch", false).apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}