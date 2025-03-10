package ai.fd.thinklet.library.proximitychecker.data.proximity

import kotlinx.coroutines.flow.Flow

sealed class ProximityState {
    data class CLOSE(val value: Float) : ProximityState()
    data class AWAY(val value: Float) : ProximityState()
}

interface ProximityRepository {
    fun startTracking(): Flow<ProximityState>
}
