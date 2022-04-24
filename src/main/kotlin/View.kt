import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun result(modifier: Modifier,calculationValue:MutableState<TextFieldValue>){
    Column(modifier = modifier.fillMaxWidth()
        .border(width = 3.dp, color = Color.DarkGray)
        .background(color = Color.White),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.End) {
        Text(text = calculationValue.value.text,
        style = TextStyle(color = Color.DarkGray, fontSize = 36.sp)
        )
    }
}

@Composable
fun buttons(modifier: Modifier,calculationValue:MutableState<TextFieldValue>){
    Column(modifier = modifier.fillMaxSize().background(color = Color.Gray)) {
        Row{
            simpleButtonView("(", calculationValue)
            simpleButtonView(")", calculationValue)
            Button(onClick= { sinFunction(calculationValue) }){ Text("sin")}
            Button(onClick= { clear(calculationValue) }){ Text("C")}
            Button(onClick= { backspace(calculationValue) }){ Text("\uF0E7")} //ikonka jesli sie da
        }
        Row{
            simpleButtonView( "7", calculationValue)
            simpleButtonView( "8", calculationValue)
            simpleButtonView( "9", calculationValue)
            simpleButtonView( "x", calculationValue)
            simpleButtonView( "sqrt", calculationValue)//zmiana
        }
        Row{
            simpleButtonView( "4", calculationValue)
            simpleButtonView( "5", calculationValue)
            simpleButtonView( "6", calculationValue)
            simpleButtonView( "÷", calculationValue)
            simpleButtonView( "%", calculationValue)//zmiana
        }
        Row{
            simpleButtonView( "1", calculationValue)
            simpleButtonView( "2", calculationValue)
            simpleButtonView( "3", calculationValue)
            simpleButtonView( "+", calculationValue)
            simpleButtonView( "1/x", calculationValue)//zmiana
        }
        Row{
            simpleButtonView( "0", calculationValue)
            simpleButtonView( "+/-", calculationValue)//zmiana
            simpleButtonView( ".", calculationValue)
            simpleButtonView( "-", calculationValue)
            Button(onClick= { equal(calculationValue) }){ Text("=")}
        }
    }
}

@Composable
fun simpleButtonView(value:String,calculationValue:MutableState<TextFieldValue>){
        Button(onClick= { onButtonClick(value, calculationValue) }){ Text(value)}
}