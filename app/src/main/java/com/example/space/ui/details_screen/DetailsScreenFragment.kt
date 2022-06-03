package com.example.space.ui.details_screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.space.R
import com.example.space.databinding.FragmentDetailsScreenBinding
import com.example.space.mvp_interfaces.DetailsScreenView
import com.example.space.presenters.DetailsScreenPresenter
import com.example.space.utils.FIRST_LAUNCH_KEY
import com.example.space.utils.PREF
import com.example.space.utils.URL_KEY
import com.example.space.utils.parseImageUrl
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Provider


@AndroidEntryPoint
class DetailsScreenFragment : MvpAppCompatFragment(R.layout.fragment_details_screen),
    DetailsScreenView {

    companion object {
        fun newInstance(url: String) = DetailsScreenFragment().apply {
            arguments  = bundleOf(URL_KEY to url)
        }
    }

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
        binding.apply {
            imageBackToMain.setOnClickListener {
                requireActivity().onBackPressed()
            }
            imageBackToMain.setOnClickListener { requireActivity().onBackPressed() }
            imageShare.setOnClickListener {
                sharePhoto()
            }
        }
        presenter.showUi(url!!)
        presenter.showOnboardingScreen()
    }

    private fun sharePhoto() {
        val bitmapDrawable = binding.imageDetailPhoto.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val imageFolder = File(requireActivity().cacheDir, "image")
        val imageUri: Uri = try {
            imageFolder.mkdirs()
            val file = File(imageFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            lifecycleScope.launch(Dispatchers.Default) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 95, stream)
                }
            }
            stream.flush()
            stream.close()
            FileProvider.getUriForFile(requireContext(), "com.example.space.fileprovider", file)
        } catch (e: Exception) {
            Snackbar.make(requireView(), getString(R.string.share_photo_error), LENGTH_LONG).show()
            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, getString(R.string.share)))
    }

    override fun showPhoto(url: String) {
        binding.imageDetailPhoto.load(url)
    }

    override fun showOnboardingScreen() {
        val settings = requireContext().getSharedPreferences(PREF, Context.MODE_PRIVATE)
        if (settings.getBoolean(FIRST_LAUNCH_KEY, true)) {
            binding.apply {
                imageBackToMain.isClickable = false
                imageShare.isClickable = false
                onboardingScreen.visibility = View.VISIBLE
            }
            lifecycleScope.launch{
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    delay(5000)
                    binding.apply {
                        imageBackToMain.isClickable = true
                        imageShare.isClickable = true
                        onboardingScreen.visibility = View.GONE
                    }
                }
            }
            binding.imageOnboardingBackground.setOnClickListener {
                binding.apply {
                    imageBackToMain.isClickable = true
                    imageShare.isClickable = true
                    onboardingScreen.visibility = View.GONE
                }
            }
            settings.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}