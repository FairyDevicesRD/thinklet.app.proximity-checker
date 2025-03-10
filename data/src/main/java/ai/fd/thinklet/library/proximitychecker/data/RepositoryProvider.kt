package ai.fd.thinklet.library.proximitychecker.data

import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityRepository
import ai.fd.thinklet.library.proximitychecker.data.proximity.impl.DefaultProximityRepositoryImpl
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.SoundEffectRepository
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.impl.SoundEffectLoader
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.impl.SoundEffectRepositoryImpl
import android.content.Context
import android.hardware.SensorManager
import android.media.AudioManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryProvider {
    @Provides
    @Singleton
    fun provideProximity(
        sensorManager: SensorManager
    ): ProximityRepository = DefaultProximityRepositoryImpl(sensorManager)

    @Provides
    @Singleton
    fun provideSensorManager(
        @ApplicationContext context: Context,
    ): SensorManager = requireNotNull(context.getSystemService(SensorManager::class.java))

    @Provides
    @Singleton
    internal fun provideSoundEffect(
        audioManager: AudioManager,
        soundEffectLoader: SoundEffectLoader
    ): SoundEffectRepository = SoundEffectRepositoryImpl(
        audioManager,
        soundEffectLoader.soundMap,
        soundEffectLoader.soundPool
    )

    @Provides
    @Singleton
    fun provideAudioManager(
        @ApplicationContext context: Context,
    ): AudioManager = requireNotNull(context.getSystemService(AudioManager::class.java))

    @Provides
    @Singleton
    internal fun provideSoundEffectLoader(
        @ApplicationContext context: Context,
    ): SoundEffectLoader = SoundEffectLoader(context)
}
