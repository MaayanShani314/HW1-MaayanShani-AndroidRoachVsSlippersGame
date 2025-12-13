package com.example.progect1_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.progect1_game.utilities.Constants
import com.example.progect1_game.utilities.SignalManager

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_cockroachLeft: AppCompatImageView

    private lateinit var main_IMG_cockroachCenter: AppCompatImageView

    private lateinit var main_IMG_cockroachRight: AppCompatImageView

    private lateinit var main_IB_left: ImageButton

    private lateinit var main_IB_right: ImageButton

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var matrix: Array<Array<AppCompatImageView>>

    private val handler: Handler = Handler(Looper.getMainLooper())

    private var gameRunning = false

    private val NUM_LANES = 3
    private var currentLane = 1

    private var lives = 3


    private val gameRunnable: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, Constants.Timer.DELAY)

            moveFlipflopsDown()
            checkCrash()
        }
    }
    private fun checkCrash() {
        val crashRow = 7

        if (matrix[crashRow][currentLane].visibility == View.VISIBLE &&
            isCockroachInLane(currentLane)) {

            removeHeart()

        }
    }

    private fun isCockroachInLane(lane: Int): Boolean {
        return when (lane) {
            0 -> main_IMG_cockroachLeft.visibility == View.VISIBLE
            1 -> main_IMG_cockroachCenter.visibility == View.VISIBLE
            2 -> main_IMG_cockroachRight.visibility == View.VISIBLE
            else -> false
        }
    }
    private fun moveFlipflopsDown() {

        for (row in 7 downTo 1) {
            for (col in 0..2) {

                if (matrix[row - 1][col].visibility == View.VISIBLE) {
                    matrix[row][col].visibility = View.VISIBLE
                } else {
                    matrix[row][col].visibility = View.INVISIBLE
                }
            }
        }

        for (col in 0..2) {
            matrix[0][col].visibility = View.INVISIBLE
        }


        if ((0..100).random() < 35) {
            val randomLane = (0..2).random()
            matrix[0][randomLane].visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initLanes()
        initButtons()
        startGame()
    }



    private fun startGame() {
        if (!gameRunning) {
            gameRunning = true
            handler.postDelayed(gameRunnable, Constants.Timer.DELAY)
        }
    }


    private fun findViews() {
        main_IMG_cockroachLeft = findViewById(R.id.main_IMG_cockroachLeft)
        main_IMG_cockroachCenter = findViewById(R.id.main_IMG_cockroachCenter)
        main_IMG_cockroachRight = findViewById(R.id.main_IMG_cockroachRight)
        main_IB_left = findViewById(R.id.main_IB_left)
        main_IB_right = findViewById(R.id.main_IB_right)

        matrix = Array(8) { row ->
            Array(3) { col ->
                val id = resources.getIdentifier("flipflop_${row}_${col}", "id", packageName)
                findViewById(id)
            }
        }

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
    }


    private fun initLanes() {
        currentLane = 1
        moveToCurrentLane()
    }

    private fun initButtons() {
        main_IB_left.setOnClickListener {
            if (currentLane > 0) {
                currentLane--
                moveToCurrentLane()
            }
        }

        main_IB_right.setOnClickListener {
            if (currentLane < NUM_LANES - 1) {
                currentLane++
                moveToCurrentLane()
            }
        }
    }

    private fun moveToCurrentLane() {
        main_IMG_cockroachLeft.visibility = View.INVISIBLE
        main_IMG_cockroachCenter.visibility = View.INVISIBLE
        main_IMG_cockroachRight.visibility = View.INVISIBLE

        when (currentLane) {
            0 -> main_IMG_cockroachLeft.visibility = View.VISIBLE
            1 -> main_IMG_cockroachCenter.visibility = View.VISIBLE
            2 -> main_IMG_cockroachRight.visibility = View.VISIBLE
        }
    }



    private fun removeHeart() {
        if (lives > 0) {
            lives--
            main_IMG_hearts[lives].visibility = View.INVISIBLE

            SignalManager.getInstance().toast("Oops!😧", SignalManager.ToastLength.SHORT)
            SignalManager.getInstance().vibrate()
        }

         if (lives == 0) {
            stopGame()
             changeActivity()
        }
    }


    private fun changeActivity() {
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
        finish()
    }

   private fun stopGame() {
        gameRunning = false
        handler.removeCallbacks(gameRunnable)

   }


    override fun onPause() {
        super.onPause()
        stopGame()
    }

    override fun onResume() {
        super.onResume()
        if (!gameRunning && lives > 0) {
            startGame()
        }
    }
}