package ai.fd.thinklet.library.proximitychecker.data.proximity.impl

import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityRepository
import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityState
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal open class DefaultProximityRepositoryImpl(
    private val sensorManager: SensorManager
) : ProximityRepository {
    private val proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    // 近接センサーの近接時の値が0.0fではない場合やvalueが複数の値を保つ場合は
    // このクラスの継承し本メソッドをオーバーライドして2値に変換するルールを別で作成する
    open fun proximityValueToState(value: Float): ProximityState {
        return if (value == 0.0f) {
            ProximityState.CLOSE(value)
        } else {
            ProximityState.AWAY(value)
        }
    }

    override fun startTracking(): Flow<ProximityState> = callbackFlow {
        if (proximitySensor == null) {
            close()
            return@callbackFlow
        }

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor.type) {
                        Sensor.TYPE_PROXIMITY -> {
                            trySend(proximityValueToState(it.values[0]))
                        }

                        else -> {}
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // do nothing
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            proximitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}

