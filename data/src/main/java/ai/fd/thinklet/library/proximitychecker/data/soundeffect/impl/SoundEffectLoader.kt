package ai.fd.thinklet.library.proximitychecker.data.soundeffect.impl

import ai.fd.thinklet.library.proximitychecker.data.R
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.SoundEffectRepository
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

typealias SoundMap = Map<SoundEffectRepository.SoundType, Int>

internal class SoundEffectLoader(context: Context) {
    val soundPool: SoundPool
    val soundMap: SoundMap

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        soundMap = mapOf(
            SoundEffectRepository.SoundType.MOUNTED to soundPool.load(
                context,
                R.raw.mounted_sound,
                1
            ),
            SoundEffectRepository.SoundType.UNMOUNTED to soundPool.load(
                context,
                R.raw.unmounted_sound,
                1
            )
        )
    }
}
