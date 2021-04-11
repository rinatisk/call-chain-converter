package expressions

import exceptions.ParseException
import exceptions.TypeException
import expressions.bool.*
import expressions.num.*
import kotlin.math.max

interface Expression

open class BinaryExpression(open val leftPart: Expression, open val rightPart: Expression, open val type: String) : Expression

open class NumberBinaryExpression(override val leftPart: Expression, override val rightPart: Expression, override val type: String) :
    BinaryExpression(leftPart, rightPart, type) {
    override fun toString(): String = "($leftPart$type$rightPart)"
}

data class PolynomialImplementation(val coefficientList: MutableList<Int>) {

    val degree
        get() = coefficientList.indexOfLast { it != 0 }

    fun equals(toCheck: PolynomialImplementation): Boolean {
        val result = true
        if (degree != toCheck.degree) return false
        for (i in 0..degree) result.and(coefficientList[i] == toCheck.coefficientList[i])
        return result
    }


    fun plus(toPlus: PolynomialImplementation): PolynomialImplementation {

        val newCoefficientList = mutableListOf<Int>()
        for (i in 0..max(degree, toPlus.degree)) {
            newCoefficientList.add(0)
        }

        val sum = PolynomialImplementation(coefficientList = newCoefficientList)

        for (i in 0..degree) {
            sum.coefficientList[i] += coefficientList[i]
        }
        for (i in 0..toPlus.degree) {
            sum.coefficientList[i] += toPlus.coefficientList[i]
        }

        return sum
    }

    fun minus(toMinus: PolynomialImplementation): PolynomialImplementation {

        val newCoefficientsList = mutableListOf<Int>()
        for (i in 0..max(degree, toMinus.degree)) {
            newCoefficientsList.add(0)
        }

        val result = PolynomialImplementation(coefficientList = newCoefficientsList)

        for (i in 0..degree) {
            result.coefficientList[i] += this.coefficientList[i]
        }
        for (i in 0..toMinus.degree) {
            result.coefficientList[i] -= toMinus.coefficientList[i]
        }

        return result
    }

    fun times(toTimes: PolynomialImplementation): PolynomialImplementation {

        val newCoefficientsList = mutableListOf<Int>()
        for (i in 0..(degree + toTimes.degree)) {
            newCoefficientsList.add(0)
        }

        val result = PolynomialImplementation(newCoefficientsList)

        for (i in 0..degree) {
            for (j in 0..toTimes.degree) {
                result.coefficientList[i + j] += coefficientList[i] * toTimes.coefficientList[j]
            }
        }

        return result
    }

    override fun toString(): String {
        // because indexOfLast return -1 when no non-zero element in list
        if (degree == -1) return "0"

        var resultString = ""

        for (i in coefficientList.indices) {
            if (coefficientList[i] != 0) {
                resultString += when {
                    i == 0 -> "${coefficientList[i]}"
                    coefficientList[i] == 1 -> "element" + "*element".repeat(i - 1)
                    else -> "(${coefficientList[i]}" + "*element".repeat(i) + ")"
                }
                if (i !=coefficientList.lastIndex) resultString += "+"
            }
        }
        return resultString
    }
}

fun newOperator(sign: String, leftExpression: Expression, rightExpression: Expression): BinaryExpression {
    if (leftExpression is Numeric && rightExpression is Numeric) {
        return when (sign) {
            "=" -> Equals(leftExpression, rightExpression)
            "+" -> Plus(leftExpression, rightExpression)
            "-" -> Minus(leftExpression, rightExpression)
            "<" -> Less(leftExpression, rightExpression)
            "*" -> Times(leftExpression, rightExpression)
            ">" -> Greater(leftExpression, rightExpression)
            else -> throw TypeException()
        }
    } else if (leftExpression is Bool && rightExpression is Bool) {
        return when (sign) {
            "|" -> Or(leftExpression, rightExpression)
            "&" -> And(leftExpression, rightExpression)
            else -> throw ParseException()
        }
    } else throw ParseException()
}

fun Expression.composition(toCompose: Expression): Expression {
    return when (this) {
        is Numeric.Element -> toCompose
        is BinaryExpression -> {
            val leftPartCompos = leftPart.composition(toCompose)
            val rightPartCompos = rightPart.composition(toCompose)
            newOperator(type, leftPartCompos, rightPartCompos)
        }
        else -> this
    }
}
