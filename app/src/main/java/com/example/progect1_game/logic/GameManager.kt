package com.example.progect1_game.logic

class GameManager(
    private val numLanes: Int,
    private val maxLives: Int
) {

    private var currentLane: Int = numLanes / 2
    private var lives: Int = maxLives
    private var coins: Int = 0
    private var odometer: Int = 0

    private var gameOver: Boolean = false

    fun getCurrentLane(): Int = currentLane

    fun getLives(): Int = lives

    fun isGameOver(): Boolean = gameOver
    fun getOdometer(): Int {
        return odometer
    }
    fun moveLeft(): Boolean {
        if (gameOver)
            return false
        if (currentLane <= 0)
            return false
        currentLane--
        return true
    }

    fun moveRight(): Boolean {
        if (gameOver)
            return false
        if (currentLane >= numLanes - 1)
            return false
        currentLane++
        return true
    }

    fun hit(): Boolean {
        if (gameOver)
            return false

        lives--

        if (lives <= 0) {
            lives = 0
            gameOver = true
        }

        return true
    }

    fun collectHeart(): Boolean {
        if (gameOver)
            return false
        if (lives >= maxLives)
            return false
        lives++
        return true
    }

    fun collectCoin() {
        if (gameOver) return
        coins += 10
    }

    fun increaseOdometer() {
        if (!gameOver) {
            odometer++
        }
    }

    fun getCoins(): Int {
        return coins
    }

}