package ai.fd.thinklet.app.proximitychecker

import ProximityScreen
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProximityScreen(viewModel)
        }

        viewModel.startProximityTracking()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (viewModel.handleKeyEvent(keyCode)) {
            true // キーイベントを消費
        } else {
            super.onKeyDown(keyCode, event) // デフォルト処理
        }
    }
}
