package ai.fd.thinklet.library.proximitychecker.data.soundeffect

interface SoundEffectRepository {
    enum class SoundType { MOUNTED, UNMOUNTED }

    fun playSound(type: SoundType)
    fun increaseVolume(type: SoundType)
    fun decreaseVolume(type: SoundType)
    fun release()
}
