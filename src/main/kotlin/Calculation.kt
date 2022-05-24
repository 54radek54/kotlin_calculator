import java.text.DecimalFormat
import kotlin.math.pow

enum class OperationType {
    ADDSUB, MUL, DIV, POW
}

enum class BracketType {
    OPENING, CLOSING
}

sealed class Token
object StartOfExpression : Token()
class Operation(val type: OperationType, var action: (Double, Double) -> Double) : Token()
class Bracket(val type: BracketType) : Token()
class Number(val number: Double) : Token()
object EndOfExpression : Token()

sealed class CalcException(message: String) : RuntimeException(message)
object MissingBracketException : CalcException("Missing bracket")
object EndOfExpressionException : CalcException("End of expression")
object NotANumberException : CalcException("Token is not a number")
object UnknownTokenException : CalcException("Token unknown")
object DivisionByZeroException : CalcException("Division by zero")

class Calculation {
    private var i = 0
    private lateinit var token: Token
    private lateinit var expr: String
    private var decimalFormat = DecimalFormat()
    private var operations = mapOf(
        '+' to Operation(OperationType.ADDSUB) { a: Double, b: Double -> a + b },
        '-' to Operation(OperationType.ADDSUB) { a: Double, b: Double -> a - b },
        '*' to Operation(OperationType.MUL) { a: Double, b: Double -> a * b },
        '/' to Operation(OperationType.DIV) { a: Double, b: Double -> a / b },
        '^' to Operation(OperationType.POW) { a: Double, b: Double -> a.pow(b) },
        '(' to Bracket(BracketType.OPENING),
        ')' to Bracket(BracketType.CLOSING)
    )

    fun evaluate(e: String): Double {
        expr = e
        i = 0
        token = StartOfExpression
        nextToken()
        return evaluateAddOrSubtract()
    }

    private fun evaluateAddOrSubtract(): Double {
        var result = evaluateMul()
        while (true) {
            val tempToken = token
            if (tempToken is Operation && tempToken.type == OperationType.ADDSUB) {
                nextToken()
                val result2 = evaluateMul()
                result = tempToken.action(result, result2)
            } else {
                break
            }
        }
        return result
    }

    private fun evaluateMul(): Double {
        var result = evaluatePow()
        while (true) {
            val tempToken = token
            if (tempToken is Operation && (tempToken.type == OperationType.MUL || tempToken.type == OperationType.DIV)) {
                nextToken()
                val result2 = evaluatePow()
                if (result2 == 0.0 && tempToken.type == OperationType.DIV)
                    throw DivisionByZeroException
                result = tempToken.action(result, result2)
            } else {
                break
            }
        }
        return result
    }

    private fun evaluatePow(): Double {
        var result = evaluateSign()
        while (true) {
            val tempToken = token
            if (tempToken is Operation && tempToken.type == OperationType.POW) {
                nextToken()
                val result2 = evaluateSign()
                result = tempToken.action(result, result2)
            } else {
                break
            }
        }
        return result
    }

    private fun evaluateSign(): Double {
        val tempToken = token
        if (tempToken is Operation && tempToken.type == OperationType.ADDSUB) {
            nextToken()
            val result = evaluateBrackets()
            return tempToken.action(0.0, result)
        }
        return evaluateBrackets()
    }

    private fun evaluateBrackets(): Double {
        val result: Double
        var tempToken = token
        if (tempToken is Bracket && tempToken.type == BracketType.OPENING) {
            nextToken()
            result = evaluateAddOrSubtract()
            tempToken = token
            if (tempToken !is Bracket || tempToken.type != BracketType.CLOSING)
                throw MissingBracketException
            nextToken()
        } else {
            result = evaluateValue()
        }
        return result
    }

    private fun evaluateValue(): Double {
        val tempToken = token
        if (tempToken is Number) {
            val result = tempToken.number
            nextToken()
            return result
        } else if (tempToken is EndOfExpression) {
            throw EndOfExpressionException
        } else if (tempToken is Bracket && tempToken.type == BracketType.CLOSING) {
            throw MissingBracketException
        }
        throw NotANumberException
    }

    private fun nextToken() {
        if (token is EndOfExpression)
            throw EndOfExpressionException

        while (true) {
            if (i == expr.length) {
                token = EndOfExpression
                return
            } else if (Character.isWhitespace(expr[i])) {
                i++
            } else {
                break
            }
        }

        if (operations.containsKey(expr[i])) {
            token = operations.getValue(expr[i])
            i++
            return
        } else if (Character.isDigit(expr[i])) {
            val start = i
            while (i < expr.length && (Character.isDigit(expr[i]) || expr[i] == decimalFormat.decimalFormatSymbols.decimalSeparator)) i++
            val number = decimalFormat.parse(expr.substring(start, i)).toDouble()
            token = Number(number)
            return
        }

        throw UnknownTokenException
    }
}