import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import org.w3c.dom.events.Event

private const val SM_BREAKPOINT = 640

@Composable
private fun rememberWindowWidth(): Int {
    var width by remember { mutableStateOf(window.innerWidth) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            width = window.innerWidth
        }
        window.addEventListener("resize", listener)
        onDispose {
            window.removeEventListener("resize", listener)
        }
    }
    return width
}

/**
 * This is the "actual" web-specific implementation
 * of the "expect" function in common code.
 */
@Composable
fun isSmallScreen(): Boolean {
    val width = rememberWindowWidth()
    return width < SM_BREAKPOINT
}
