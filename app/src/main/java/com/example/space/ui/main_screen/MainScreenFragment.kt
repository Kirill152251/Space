package com.example.space.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.api_response_model.Photo
import com.example.space.R
import com.example.space.databinding.FragmentMainScreenBinding
import com.example.space.mvp_interfaces.MainScreenView
import com.example.space.presenters.MainScreenPresenter
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainScreenFragment : MvpAppCompatFragment(R.layout.fragment_main_screen), MainScreenView {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenterProvider: Provider<MainScreenPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomMenu =
            requireActivity().findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.layout_bottom_menu)
        bottomMenu.visibility = View.VISIBLE
        val adapter = MainScreenAdapter() {
            itemClickListener(it)
        }
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPhotos.adapter = adapter
        binding.rvPhotos.layoutManager = gridLayoutManager
        presenter.showUi(adapter)

    }

    private fun itemClickListener(photo: Photo) {
        presenter.openDetailsScreen(photo.image)
    }

    override fun showRecycleView(photos: PagingData<Photo>, adapter: MainScreenAdapter) {
            adapter.submitData(lifecycle, photos)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}