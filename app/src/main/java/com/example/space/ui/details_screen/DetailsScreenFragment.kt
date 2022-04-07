package com.example.space.ui.details_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.space.R
import com.example.space.databinding.FragmentDetailsScreenBinding
import com.example.space.databinding.FragmentMainScreenBinding
import com.example.space.mvi_interfaces.details_screen.DetailsScreenView
import com.example.space.presenters.DetailsScreenPresenter
import com.example.space.presenters.MainScreenPresenter
import com.example.space.utils.URL_KEY
import com.example.space.utils.parseImageUrl
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


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
        presenter.showUi(url!!)
    }

    override fun showPhoto(url: String) {
        binding.imageDetailPhoto.load(url) {
            crossfade(true)
            crossfade(300)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}