import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sin
import kotlin.math.sqrt

val CHECK = arrayOf("+", "-", "x", "÷", ".", "^")
val VAL = arrayOf("x", "÷", ".", "^")
val NUMBERS = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

fun calculate(input: String): String? {
    if (input == "Missing bracket" || input == "End of expression" || input == "Token is not a number" || input == "Token unknown" || input == "Division by zero") {
        return null
    }
    if ((CHECK.any { input.endsWith(it) })) {
        return null
    }
    return try {
        val expressionToCalculate = input.replace('x', '*').replace('÷', '/').replace('.', ',')
        val calculation = Calculation()
        BigDecimal(calculation.evaluate(expressionToCalculate))
            .round(MathContext(10, RoundingMode.HALF_UP))
            .stripTrailingZeros().toPlainString()
    } catch (e: Exception) {
        return e.message
    }
}

fun onButtonClick(value: String, calculationValue: MutableState<TextFieldValue>) {
    val input = calculationValue.value.text
    if (!(input === "" && VAL.any { value === it })) {
        if (!(value === "(" && NUMBERS.any { input.endsWith(it) || input.endsWith(".") })) {
            if (input != "Missing bracket" && input != "End of expression" && input != "Token is not a number" && input != "Token unknown" && input != "Division by zero") {
                if (!(CHECK.any { value == it } && CHECK.any { calculationValue.value.text.endsWith(it) })) {
                    if (value == ")") {
                        if (calculationValue.value.text.contains("(")) {
                            calculationValue.value = TextFieldValue(calculationValue.value.text.plus(value))
                        }
                    } else {
                        calculationValue.value = TextFieldValue(calculationValue.value.text.plus(value))
                    }
                }
            }
        }
    }
}

fun equal(calculationValue: MutableState<TextFieldValue>, expressionValue: MutableState<TextFieldValue>) {
    if (!(CHECK.any { calculationValue.value.text.endsWith(it) })) {
        expressionValue.value = TextFieldValue(calculationValue.value.text)
        calculate(calculationValue.value.text)?.let { value ->
            calculationValue.value = TextFieldValue(value)
        }
    }

}

fun clear(calculationValue: MutableState<TextFieldValue>, expressionValue: MutableState<TextFieldValue>) {
    calculationValue.value = TextFieldValue("")
    expressionValue.value = TextFieldValue("")
}

fun backspace(calculationValue: MutableState<TextFieldValue>) {
    val input = calculationValue.value.text
    if (input != "Missing bracket" && input != "End of expression" && input != "Token is not a number" && input != "Token unknown" && input != "Division by zero") {
        if (calculationValue.value.text != "") {
            calculationValue.value = TextFieldValue(
                if (calculationValue.value.text.length != 1) {
                    calculationValue.value.text.dropLast(1)
                } else ""
            )
        }
    }
}

fun sinValue(calculationValue: MutableState<TextFieldValue>, expressionValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        sin(calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            expressionValue.value = TextFieldValue("sin(".plus(calculationValue.value.text).plus(")"))
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}

fun power(calculationValue: MutableState<TextFieldValue>) {
    val input = calculationValue.value.text
    if (!(input === "")) {
        if (input != "Missing bracket" && input != "End of expression" && input != "Token is not a number" && input != "Token unknown" && input != "Division by zero") {
            if (!(CHECK.any { calculationValue.value.text.endsWith(it) })) {
                calculationValue.value = TextFieldValue(calculationValue.value.text.plus("^"))
            }
        }
    }
}

fun root(calculationValue: MutableState<TextFieldValue>, expressionValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        sqrt(calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            expressionValue.value = TextFieldValue("sqrt(".plus(calculationValue.value.text).plus(")"))
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}

fun inverse(calculationValue: MutableState<TextFieldValue>, expressionValue: MutableState<TextFieldValue>) {
    if (calculate(calculationValue.value.text) != null) {
        (1 / calculate(calculationValue.value.text)!!.toDouble()).let { value ->
            expressionValue.value = TextFieldValue("1/".plus(calculationValue.value.text))
            calculationValue.value = TextFieldValue(value.toString())
        }
    }
}

fun signChange(calculationValue: MutableState<TextFieldValue>) {
    val input = calculationValue.value.text
    if (input != "Missing bracket" && input != "End of expression" && input != "Token is not a number" && input != "Token unknown" && input != "Division by zero") {
        if (calculationValue.value.text != "") {
            if (calculationValue.value.text.startsWith("-")) {
                calculationValue.value.text.removePrefix("-").let { value ->
                    calculationValue.value = TextFieldValue(value)
                }
            } else {
                "-".plus(calculationValue.value.text).let { value ->
                    calculationValue.value = TextFieldValue(value)
                }
            }
        }
    }
}



