package ai.fd.thinklet.app.proximitychecker

import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityRepository
import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityState
import ai.fd.thinklet.library.proximitychecker.data.soundeffect.SoundEffectRepository
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(val proximity: ProximityState?, val key: String)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val proximityRepository: ProximityRepository,
    private val soundEffectRepository: SoundEffectRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _proximityState = MutableStateFlow<ProximityState?>(null)
    private val proximityState = _proximityState.asStateFlow()

    private val _pressedKey = MutableStateFlow<String>("") // 押されたキーを管理
    private val pressedKey = _pressedKey.asStateFlow()

    // 近接センサーの値かキー押下があればUIを更新する
    val uiState: StateFlow<UiState> = combine(proximityState, pressedKey) { proximity, key ->
        UiState(proximity, key)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = UiState(null, "")
    )

    // アプリがフォアグラウンドにあるかどうか
    private val _isAppInForeground = MutableStateFlow(true)
    //val isAppInForeground = _isAppInForeground.asStateFlow()

    init {
        // アプリのライフサイクルを監視
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        _isAppInForeground.value = true
    }

    override fun onStop(owner: LifecycleOwner) {
        _isAppInForeground.value = false
    }

    fun startProximityTracking() {
        viewModelScope.launch {
            proximityRepository.startTracking().collectLatest { value ->
                _proximityState.value = value
                Log.i(TAG, "value = $value")

                if (_isAppInForeground.value) {
                    soundEffectRepository.playSound(currentMountedStateToSoundType())
                }
            }
        }
    }

    fun handleKeyEvent(keyCode: Int): Boolean {
        val soundType = currentMountedStateToSoundType()

        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                soundEffectRepository.increaseVolume(soundType)
                _pressedKey.value = "Volume Up"
                true
            }

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                soundEffectRepository.decreaseVolume(soundType)
                _pressedKey.value = "Volume Down"
                true
            }

            KeyEvent.KEYCODE_CAMERA -> {
                soundEffectRepository.playSound(soundType)
                _pressedKey.value = "Camera Key"
                true
            }

            else -> false
        }
    }

    private fun currentMountedStateToSoundType(): SoundEffectRepository.SoundType {
        return when (proximityState.value) {
            is ProximityState.CLOSE -> SoundEffectRepository.SoundType.MOUNTED
            else -> SoundEffectRepository.SoundType.UNMOUNTED
        }
    }


    override fun onCleared() {
        super.onCleared()
        soundEffectRepository.release()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    private companion object {
        const val TAG = "MainViewModel"
    }
}
