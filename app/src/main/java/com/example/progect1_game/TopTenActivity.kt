package com.example.progect1_game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.progect1_game.ui.MapFragment
import com.example.progect1_game.ui.TopTenFragment
import com.example.progect1_game.interfaces.CallbackTopTenClicked

class TopTenActivity : AppCompatActivity() {

    private lateinit var mapFragment: MapFragment
    private lateinit var topTenFragment: TopTenFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_top_ten)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            mapFragment = MapFragment()
            topTenFragment = TopTenFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_FRAME_topTen, TopTenFragment())
                .commit()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_FRAME_map, mapFragment)
                .commit()

            TopTenFragment.callback = object : CallbackTopTenClicked {
                override fun onItemClicked(name: String, lat: Double, lon: Double) {
                    mapFragment.showLocation(name, lat, lon)
                }
            }
        }
    }
}
