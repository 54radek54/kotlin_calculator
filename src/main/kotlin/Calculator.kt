import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val OPERATIONS = arrayOf("+", "-", "*", "/")

fun calculate(input: String): String? {
    val expressionToCalculate = input.replace('x', '*').replace('÷', '/').replace('.',',')
    if (OPERATIONS.any { expressionToCalculate.endsWith(it) }) {
        return null
    }
    return try {
        val calculation=Calculation()
        BigDecimal(calculation.evaluate(expressionToCalculate))
            .round(MathContext(8, RoundingMode.HALF_UP))
            .stripTrailingZeros().toPlainString()
    } catch (e: Exception) {
        null
    }
}

fun onButtonClick(value: String, calculationValue: MutableState<TextFieldValue>) {
    calculationValue.value = TextFieldValue(calculationValue.value.text.plus(value))
}

fun equal(calculationValue: MutableState<TextFieldValue>) {
    calculate(calculationValue.value.text)?.let { value ->
        calculationValue.value = TextFieldValue(value)
    }
}

fun clear(calculationValue: MutableState<TextFieldValue>) {
    calculationValue.value = TextFieldValue("")
}

fun backspace(calculationValue: MutableState<TextFieldValue>) {
    if (calculationValue.value.text != "") {
        calculationValue.value = TextFieldValue(
            if (calculationValue.value.text.length != 1) {
                calculationValue.value.text.dropLast(1)
            } else ""
        )
    }
}

fun sinValue(calculationValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        sin(calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}

fun power(calculationValue: MutableState<TextFieldValue>) {
    calculationValue.value = TextFieldValue(calculationValue.value.text.plus("^"))
}

fun root(calculationValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        sqrt(calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}

fun inverse(calculationValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        (1 / calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}
//TODO do poprawy jeszcze
fun signChange(calculationValue: MutableState<TextFieldValue>) {
    if(calculationValue.value.text!=""){
        if(calculationValue.value.text.startsWith("-")){
            calculationValue.value.text.removePrefix("-").let { value ->
                calculationValue.value = TextFieldValue(value)
            }
        }else{
            "-".plus(calculationValue.value.text).let { value ->
                calculationValue.value = TextFieldValue(value)
            }
        }
    }

}



