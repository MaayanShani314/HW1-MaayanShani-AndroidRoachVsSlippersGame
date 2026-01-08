package com.example.progect1_game

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.progect1_game.logic.TopTenManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import androidx.appcompat.app.AlertDialog


class GameOverActivity : AppCompatActivity() {


    private lateinit var gameOver_LBL_title: MaterialTextView

    private lateinit var score_BTN_newGame: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gameover)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
        showNameDialog()

    }

    private fun findViews() {
        gameOver_LBL_title = findViewById(R.id.gameOver_LBL_title)
        score_BTN_newGame = findViewById(R.id.score_BTN_newGame)
    }

    private fun initViews() {
        score_BTN_newGame.setOnClickListener { view: View ->
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveScore(name: String) {
        val odometer = intent.getIntExtra("EXTRA_ODOMETER", 0)

        TopTenManager
            .getInstance()
            .addScore(name, odometer)
    }

    private fun showNameDialog() {
        val odometer = intent.getIntExtra("EXTRA_ODOMETER", 0)

        val titleView = TextView(this).apply {
            text = "GAME OVER"
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 24)
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            gravity = Gravity.START
            setPadding(48, 16, 48, 0)
        }

        val scoreText = TextView(this).apply {
            text = "Your score: $odometer meters"
            textSize = 16f
            gravity = Gravity.START
        }

        val nameLabel = TextView(this).apply {
            text = "Enter your name:"
            textSize = 16f
            gravity = Gravity.START
            setPadding(0, 24, 0, 8)
        }

        val input = EditText(this).apply {
            hint = "Enter your name"
            gravity = Gravity.START
            layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        container.addView(scoreText)
        container.addView(nameLabel)
        container.addView(input)

        AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString().ifBlank { "Player" }
                saveScore(name)
            }
            .show()
    }
}
