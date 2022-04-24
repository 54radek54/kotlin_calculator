import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sin

val OPERATIONS = arrayOf("+", "-", "*", "/")

fun calculate(input: String): String? {
    val expressionToCalculate = input.replace('x', '*').replace('÷', '/')
    if (!OPERATIONS.any { expressionToCalculate.contains(it) } || OPERATIONS.any { expressionToCalculate.endsWith(it) }) {
        return null
    }
    return try {
        BigDecimal(Expression(expressionToCalculate).calculate())
            .round(MathContext(8, RoundingMode.HALF_UP))
            .stripTrailingZeros().toPlainString()
    } catch (e: Exception) {
        null
    }
}

fun onButtonClick(value: String,calculationValue: MutableState<TextFieldValue>) {
    calculationValue.value = TextFieldValue(calculationValue.value.text.plus(value))
}

fun equal(calculationValue: MutableState<TextFieldValue>){
    calculate(calculationValue.value.text)?.let { value->
        calculationValue.value= TextFieldValue(value) }
}

fun clear(calculationValue: MutableState<TextFieldValue>){
    calculationValue.value = TextFieldValue("")
}

fun sinFunction(calculationValue: MutableState<TextFieldValue>){
    sin(calculationValue.value.text.toDouble()).let { value->
        calculationValue.value= TextFieldValue(value.toString()) }
}

fun backspace(calculationValue: MutableState<TextFieldValue>){
        if(calculationValue.value.text!=""){
        calculationValue.value = TextFieldValue(
            if(calculationValue.value.text.length!=1){
                calculationValue.value.text.dropLast(1)
            } else "") }
}


