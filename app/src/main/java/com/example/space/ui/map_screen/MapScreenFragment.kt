package com.example.space.ui.map_screen

import androidx.appcompat.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import com.example.space.R
import com.example.space.databinding.FragmentMapScreenBinding
import com.example.space.mvi_interfaces.MapScreenView
import com.example.space.presenters.MapScreenPresenter
import com.example.space.utils.MAP_TYPE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MapScreenFragment : MvpAppCompatFragment(R.layout.fragment_map_screen), OnMapReadyCallback,
    MapScreenView {

    private var _binding: FragmentMapScreenBinding? = null
    private val binding get() = _binding!!

    private var map: GoogleMap? = null

    @Inject
    lateinit var presenterProvider: Provider<MapScreenPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    @Inject
    lateinit var sharePref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapScreenBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageChangeMod.setOnClickListener {
            presenter.showMapTypeDialog(sharePref.getInt(MAP_TYPE, 0))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.style_map
            )
        )
        when(sharePref.getInt(MAP_TYPE, 0)) {
            0 -> googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            1 -> googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            2 -> googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        val moscow = LatLng(55.7558, 37.6173)
        googleMap.addMarker(
            MarkerOptions().position(moscow)
                .icon(getBitmapFromDrawable())
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
    }

    private fun getBitmapFromDrawable(): BitmapDescriptor {
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.custom_marker)!!
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun showMapTypeDialog(mapType: Int) {

        //Это костыль, но по другому у меня не получилось поменять цвет текста напротив radio buttons.
        //Я пробовал добавить в стиль для диалога: "android:textColorAlertDialogListItem", просто "android:textColor" - всё не работает.
        val options = arrayOf(
            HtmlCompat.fromHtml(
                "<font color='#C4C4C4'>${getString(R.string.mode_normal)}</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ),
            HtmlCompat.fromHtml(
                "<font color='#C4C4C4'>${getString(R.string.mode_satellite)}</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ),
            HtmlCompat.fromHtml(
                "<font color='#C4C4C4'>${getString(R.string.mode_hybrid)}</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(getString(R.string.map_type_dialog_title))
            .setSingleChoiceItems(options, mapType, null)
            .setPositiveButton(getString(R.string.positive_button)) { dialog, _ ->
                when ((dialog as AlertDialog).listView.checkedItemPosition) {
                    0 -> {
                        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        sharePref.edit().putInt(MAP_TYPE, 0).apply()
                    }
                    1 -> {
                        map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        sharePref.edit().putInt(MAP_TYPE, 1).apply()
                    }
                    2 -> {
                        map?.mapType = GoogleMap.MAP_TYPE_HYBRID
                        sharePref.edit().putInt(MAP_TYPE, 2).apply()
                    }
                }
            }
            .setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            .create()
        dialog.show()
    }
}