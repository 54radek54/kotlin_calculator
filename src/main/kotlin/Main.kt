import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    val calculationValue = remember { mutableStateOf(TextFieldValue("")) }
    Window(onCloseRequest = ::exitApplication,
        title = "Kotlin calculator",
        resizable = false,
        state = WindowState(size = DpSize.Unspecified)){
        MaterialTheme {
            Column(Modifier.size(320.dp, 305.dp)) {
                result(Modifier.weight(1f), calculationValue)
                buttons(Modifier.weight(5f), calculationValue)
            }
        }
    }
}
