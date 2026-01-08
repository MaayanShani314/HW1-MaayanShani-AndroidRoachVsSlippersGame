package com.example.progect1_game

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CONTROL_MODE = "EXTRA_CONTROL_MODE"
        const val CONTROL_BUTTONS = "CONTROL_BUTTONS"
        const val CONTROL_SENSORS = "CONTROL_SENSORS"
    }

    private lateinit var menu_BTN_buttonMode: MaterialButton
    private lateinit var menu_BTN_sensorMode: MaterialButton
    private lateinit var menu_BTN_topTen: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_buttonMode = findViewById(R.id.menu_BTN_buttonMode)
        menu_BTN_sensorMode = findViewById(R.id.menu_BTN_sensorMode)
        menu_BTN_topTen = findViewById(R.id.menu_BTN_topTen)
    }

    private fun initViews() {
        menu_BTN_buttonMode.setOnClickListener {
            startGame(CONTROL_BUTTONS)
        }

        menu_BTN_sensorMode.setOnClickListener {
            startGame(CONTROL_SENSORS)
        }

        menu_BTN_topTen.setOnClickListener {
            val intent = Intent(this, TopTenActivity::class.java)
            startActivity(intent)
        }
    }



    private fun startGame(mode: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_CONTROL_MODE, mode)
        startActivity(intent)
        finish()
    }
}
