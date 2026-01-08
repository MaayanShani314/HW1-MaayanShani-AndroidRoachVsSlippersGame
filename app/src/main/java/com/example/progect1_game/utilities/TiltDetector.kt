package com.example.progect1_game.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.progect1_game.interfaces.TiltCallback

class TiltDetector(
    context: Context,
    private val tiltCallback: TiltCallback
) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var timestamp: Long = 0L

    private val sensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]

            if (System.currentTimeMillis() - timestamp < 300) return
            timestamp = System.currentTimeMillis()

            when {
                x > 3 -> tiltCallback.tiltLeft()
                x < -3 -> tiltCallback.tiltRight()
            }

            when {
                y > 3 -> tiltCallback.tiltForward()
                y < -3 -> tiltCallback.tiltBackward()
                else -> tiltCallback.tiltNeutral()
            }
        }
    }

    fun start() {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
