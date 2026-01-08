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
import com.example.progect1_game.interfaces.TiltCallback
import com.example.progect1_game.logic.GameManager
import com.example.progect1_game.utilities.Constants
import com.example.progect1_game.utilities.GameObject
import com.example.progect1_game.utilities.SignalManager
import com.example.progect1_game.utilities.SingleSoundPlayer
import com.example.progect1_game.utilities.TiltDetector
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var cockroachViews: Array<AppCompatImageView>

    private lateinit var main_IB_left: ImageButton

    private lateinit var main_IB_right: ImageButton

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_LBL_coins: MaterialTextView


    private lateinit var matrix: Array<Array<AppCompatImageView>>

    private lateinit var objectMatrix: Array<Array<Int>>

    private lateinit var soundPlayer: SingleSoundPlayer


    private val handler: Handler = Handler(Looper.getMainLooper())

    private var gameRunning = false

    private val NUM_LANES = 5
    private val MAX_LIVES = 3

    private lateinit var gameManager: GameManager

    private var tiltDetector: TiltDetector? = null
    private var isSensorMode = false

    private var currentDelay = Constants.Timer.NORMAL_DELAY




    private val gameRunnable: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, currentDelay)
            gameManager.increaseOdometer()
            moveFlipflopsDown()
            checkCrash()
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

        gameManager = GameManager(NUM_LANES, MAX_LIVES)
        objectMatrix = Array(8) { Array(NUM_LANES) { GameObject.EMPTY } }
        soundPlayer = SingleSoundPlayer(this)


        findViews()
        applyControlMode()
        initButtons()
        startGame()
    }

    private fun checkCrash() {
        val crashRow = 7
        val lane = gameManager.getCurrentLane()
        val obj = objectMatrix[crashRow][lane]

        when (obj) {

            GameObject.FLIPFLOP -> {
                if (gameManager.hit()) {
                    updateHearts()
                    soundPlayer.playSound(R.raw.smash)
                    SignalManager.getInstance().vibrate()
                    SignalManager.getInstance()
                        .toast("Oops! 😧", SignalManager.ToastLength.SHORT)
                }
            }

            GameObject.COIN -> {
                gameManager.collectCoin()
                updateCoins()
                soundPlayer.playSound(R.raw.collectcoins)
                SignalManager.getInstance().toast("🪙 +10", SignalManager.ToastLength.SHORT)
            }

            GameObject.HEART -> {
                if (gameManager.collectHeart()) {
                    updateHearts()
                    soundPlayer.playSound(R.raw.yay)
                    SignalManager.getInstance().toast("❤️ +1", SignalManager.ToastLength.SHORT)
                }
            }
        }

        objectMatrix[crashRow][lane] = GameObject.EMPTY
        matrix[crashRow][lane].visibility = View.INVISIBLE

        if (gameManager.isGameOver()) {
            stopGame()
            changeActivity()
        }
    }

    private fun updateHearts() {
        val lives = gameManager.getLives()
        main_IMG_hearts.forEachIndexed { index, imageView ->
            imageView.visibility = if (index < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun moveFlipflopsDown() {

        for (row in 7 downTo 1) {
            for (col in 0 until NUM_LANES) {
                objectMatrix[row][col] = objectMatrix[row - 1][col]
                matrix[row][col].visibility = matrix[row - 1][col].visibility
                matrix[row][col].setImageDrawable(matrix[row - 1][col].drawable)
            }
        }

        for (col in 0 until NUM_LANES) {
            objectMatrix[0][col] = GameObject.EMPTY
            matrix[0][col].visibility = View.INVISIBLE
        }

        if ((0..100).random() < 35) {
            val lane = (0 until NUM_LANES).random()
            val roll = (0..100).random()

            val type = when {
                roll < 50 -> GameObject.FLIPFLOP
                roll < 80 -> GameObject.COIN
                else -> {
                    if (gameManager.getLives() < MAX_LIVES)
                        GameObject.HEART
                    else
                        GameObject.COIN
                }
            }

            objectMatrix[0][lane] = type
            matrix[0][lane].visibility = View.VISIBLE

            matrix[0][lane].setImageResource(
                when (type) {
                    GameObject.FLIPFLOP -> R.drawable.flipflop
                    GameObject.COIN -> R.drawable.coin
                    GameObject.HEART -> R.drawable.heart2
                    else -> 0
                }
            )
        }
    }


    private fun startGame() {
        if (!gameRunning) {
            gameRunning = true
            handler.postDelayed(gameRunnable, currentDelay)
        }
    }


    private fun findViews() {

        cockroachViews = arrayOf(
            findViewById(R.id.main_IMG_cockroach0),
            findViewById(R.id.main_IMG_cockroach1),
            findViewById(R.id.main_IMG_cockroach2),
            findViewById(R.id.main_IMG_cockroach3),
            findViewById(R.id.main_IMG_cockroach4)
        )

        main_IB_left = findViewById(R.id.main_IB_left)
        main_IB_right = findViewById(R.id.main_IB_right)

        matrix = Array(8) { row ->
            Array(NUM_LANES) { col ->
                val id = resources.getIdentifier("flipflop_${row}_${col}", "id", packageName)
                findViewById(id)
            }
        }

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        main_LBL_coins = findViewById(R.id.main_LBL_coins)

    }

    private fun initButtons() {
        main_IB_left.setOnClickListener {
            if (gameManager.moveLeft()) {
                updateCockroachPosition()
            }
        }

        main_IB_right.setOnClickListener {
            if (gameManager.moveRight()) {
                updateCockroachPosition()
            }
        }
    }

    private fun updateCockroachPosition() {
        cockroachViews.forEach { it.visibility = View.INVISIBLE }
        cockroachViews[gameManager.getCurrentLane()].visibility = View.VISIBLE
    }

    private fun changeActivity() {
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("EXTRA_ODOMETER", gameManager.getOdometer())
        startActivity(intent)
        finish()
    }

    private fun applyControlMode() {
        val mode = intent.getStringExtra(MenuActivity.EXTRA_CONTROL_MODE)
            ?: MenuActivity.CONTROL_BUTTONS

        isSensorMode = mode == MenuActivity.CONTROL_SENSORS

        if (isSensorMode) {
            main_IB_left.visibility = View.INVISIBLE
            main_IB_right.visibility = View.INVISIBLE
            initTiltDetector()
        } else {
            main_IB_left.visibility = View.VISIBLE
            main_IB_right.visibility = View.VISIBLE
        }
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(this, object : TiltCallback {

            override fun tiltLeft() {
                if (gameManager.moveLeft()) updateCockroachPosition()
            }

            override fun tiltRight() {
                if (gameManager.moveRight()) updateCockroachPosition()
            }

            override fun tiltForward() {
                currentDelay = Constants.Timer.FAST_DELAY
            }

            override fun tiltBackward() {
                currentDelay = Constants.Timer.SLOW_DELAY
            }

            override fun tiltNeutral() {
                currentDelay = Constants.Timer.NORMAL_DELAY
            }
        })
    }

    private fun updateCoins() {
        main_LBL_coins.text = "Coins: ${gameManager.getCoins()}"
    }

    private fun stopGame() {
        gameRunning = false
        handler.removeCallbacks(gameRunnable)

   }

    override fun onResume() {
        super.onResume()
        if (isSensorMode)
            tiltDetector?.start()
        if (!gameRunning && !gameManager.isGameOver())
            startGame()
    }

    override fun onPause() {
        super.onPause()
        tiltDetector?.stop()
        stopGame()
    }
}