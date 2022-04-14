package com.example.space.ui.map_screen

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.space.R
import com.example.space.databinding.FragmentMapScreenBinding
import com.example.space.mvi_interfaces.MapScreenView
import com.example.space.presenters.MapScreenPresenter
import com.example.space.utils.MAP_TYPE
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
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

    private var adapter: MarkersAdapter? = null

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
        val bottomSheetLayout =
            getView()?.findViewById<LinearLayout>(R.id.bottom_sheet_layout) as View
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.imageMarkerManager.setOnClickListener {
            presenter.showMarkerManagerBottomSheet(bottomSheetBehavior)
        }

        val markersRecyclerView = getView()?.findViewById<RecyclerView>(R.id.rv_markers)
        adapter = MarkersAdapter {
            deleteMarker(it)
        }
        markersRecyclerView?.adapter = adapter
        markersRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun deleteMarker(position: Int) {
        val currentList = adapter?.currentList?.toMutableList() ?: mutableListOf()
        if (currentList.size == 1) {
            currentList.removeAt(0)
            adapter?.submitList(currentList)
            map?.clear()
            return
        }
        if (position == currentList.size) {
            currentList.removeAt(position - 1)
            adapter?.submitList(currentList)
            map?.clear()
            for (item in currentList) {
                map?.addMarker(
                    MarkerOptions().position(item.latLng)
                        .icon(getBitmapMarkerIconFromDrawable())
                        .title(item.name)
                )
            }
        } else {
            currentList.removeAt(position)
            adapter?.submitList(currentList)
            map?.clear()
            for (item in currentList) {
                map?.addMarker(
                    MarkerOptions().position(item.latLng)
                        .icon(getBitmapMarkerIconFromDrawable())
                        .title(item.name)
                )
            }
        }
    }

    private fun setMarkerOnTheMap(latLng: LatLng) {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.edit_text_marker_name)


        with(dialogBuilder) {
            setPositiveButton(getString(R.string.save_positive_button)) { _, _ ->
                val markerName = editText.text.toString()
                if (markerName.isEmpty()) {
                    Snackbar.make(requireView(), getString(R.string.input_error), LENGTH_LONG)
                        .show()
                    setMarkerOnTheMap(latLng)
                } else {
                    map?.addMarker(
                        MarkerOptions().position(latLng)
                            .icon(getBitmapMarkerIconFromDrawable())
                            .title(markerName)
                    )
                    val marker = MapMarker(markerName, latLng)
                    val currentList = adapter?.currentList?.toMutableList()
                    currentList?.add(marker)
                    adapter?.submitList(currentList)
                }
            }
            setNegativeButton(getString(R.string.negative_button)) { _, _ -> }
            setView(dialogLayout)
            show()
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
        when (sharePref.getInt(MAP_TYPE, 0)) {
            0 -> googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            1 -> googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            2 -> googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        googleMap.setOnMapClickListener {
            setMarkerOnTheMap(it)
        }
    }

    private fun getBitmapMarkerIconFromDrawable(): BitmapDescriptor {
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

    override fun showMarkerManagerBottomSheet(bottomSheetBehavior: BottomSheetBehavior<View>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }
}