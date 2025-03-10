import ai.fd.thinklet.app.proximitychecker.MainViewModel
import ai.fd.thinklet.app.proximitychecker.ui.theme.ProximitySensorCheckerTheme
import ai.fd.thinklet.library.proximitychecker.data.proximity.ProximityState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProximityScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // 背景色とテキストカラーの決定
    val (backgroundColor, textColor, statusText) = when (uiState.proximity) {
        is ProximityState.CLOSE -> Triple(
            Color.Green,
            Color.White,
            "MOUNTED"
        ) // MOUNTED: 背景緑 + 黒テキスト
        null -> Triple(Color.Gray, Color.White, "Waiting...") // 初期状態
        else -> Triple(Color.Red, Color.White, "UNMOUNTED") // UNMOUNTED: 背景赤 + 白テキスト
    }

    ProximitySensorCheckerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 見出し
                Text(
                    text = "Proximity Sensor",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = shadowedTextStyle()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 状態表示 (MOUNTED / UNMOUNTED)
                Text(
                    text = statusText,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = shadowedTextStyle()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 数値表示 (value = 0.0)
                Text(
                    text = "value = ${uiState.proximity ?: "?"}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = shadowedTextStyle()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "pressed key = ${uiState.key}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = shadowedTextStyle()
                )
            }
        }
    }
}

// **影を適用するテキストスタイル**
@Composable
fun shadowedTextStyle() = TextStyle(
    shadow = Shadow(
        color = Color.Black.copy(alpha = 0.5f),
        offset = Offset(4f, 4f),
        blurRadius = 4f
    )
)
