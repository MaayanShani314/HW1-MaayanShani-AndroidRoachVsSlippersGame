package com.example.progect1_game.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progect1_game.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fp) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val initial = LatLng(32.0853, 34.7818)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 10f))
    }


    fun showLocation(name: String, lat: Double, lon: Double) {
        val pos = LatLng(lat, lon)

        googleMap.addMarker(
            MarkerOptions()
                .position(pos)
                .title(name)
        )

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(pos, 12f)
        )
    }
}

