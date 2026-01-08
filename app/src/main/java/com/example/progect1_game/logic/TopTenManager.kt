package com.example.progect1_game.logic

import com.example.progect1_game.utilities.SharedPreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TopTenManager private constructor() {

    private val gson = Gson()
    private val key = "TOP_TEN"
    private val maxSize = 10

    private val scores: MutableList<Score> = mutableListOf()

    companion object {
        @Volatile
        private var instance: TopTenManager? = null

        fun getInstance(): TopTenManager {
            return instance ?: synchronized(this) {
                instance ?: TopTenManager().also {
                    it.load()
                    instance = it
                }
            }
        }
    }

    private fun load() {
        val json = SharedPreferencesManager
            .getInstance()
            .getString(key, "")

        if (json.isNotEmpty()) {
            val type = object : TypeToken<MutableList<Score>>() {}.type
            val savedScores: MutableList<Score> = gson.fromJson(json, type)
            scores.clear()
            scores.addAll(savedScores)
        }
    }

    private fun save() {
        val json = gson.toJson(scores)
        SharedPreferencesManager
            .getInstance()
            .putString(key, json)
    }

    fun addScore(name: String, odometer: Int) {

        val index = scores.size
        val (lat, lon) = generateSafeLocationInIsrael(index)

        scores.add(
            Score(
                name = name,
                odometer = odometer,
                lat = lat,
                lon = lon
            )
        )

        scores.sortByDescending { it.odometer }

        if (scores.size > maxSize)
            scores.subList(maxSize, scores.size).clear()

        save()
    }


    fun getScores(): List<Score> = scores.toList()

    fun clear() {
        scores.clear()
        save()
    }


    private fun generateSafeLocationInIsrael(index: Int): Pair<Double, Double> {

        val cities = listOf(
            Pair(32.0853, 34.7818),
            Pair(31.7683, 35.2137),
            Pair(32.7940, 34.9896),
            Pair(31.2529, 34.7915),
            Pair(32.3215, 34.8532),
            Pair(31.8044, 34.6553)
        )

        val baseCity = cities[index % cities.size]

        val radius = 0.003
        val angle = index * 45.0 * Math.PI / 180.0

        val latOffset = radius * Math.cos(angle)
        val lonOffset = radius * Math.sin(angle)

        return Pair(
            baseCity.first + latOffset,
            baseCity.second + lonOffset
        )
    }

}



