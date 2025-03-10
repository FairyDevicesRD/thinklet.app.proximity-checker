package ai.fd.thinklet.library.proximitychecker.data.soundeffect.impl

import ai.fd.thinklet.library.proximitychecker.data.soundeffect.SoundEffectRepository
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.SoundEffectRepository.SoundType
import android.media.AudioManager
import android.media.SoundPool

internal class SoundEffectRepositoryImpl(
    private val audioManager: AudioManager,
    private val soundMap: SoundMap,
    private val soundPool: SoundPool
) : SoundEffectRepository {

    private var currentVolume: Float = 0.0f

    init {
        syncVolumeWithSystem()
    }

    override fun playSound(type: SoundType) {
        soundMap[type]?.let { soundId ->
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    override fun increaseVolume(type: SoundType) {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_RAISE,
            AudioManager.FLAG_SHOW_UI
        )
        syncVolumeWithSystem()
        playSound(type)
    }

    override fun decreaseVolume(type: SoundType) {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER,
            AudioManager.FLAG_SHOW_UI
        )
        syncVolumeWithSystem()
        playSound(type)
    }

    private fun syncVolumeWithSystem() {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentSystemVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        currentVolume = currentSystemVolume / maxVolume.toFloat()
    }

    override fun release() {
        soundPool.release()
    }
}
