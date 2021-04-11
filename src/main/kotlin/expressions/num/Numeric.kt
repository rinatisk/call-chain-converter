package expressions.num

import expressions.Expression
import expressions.NumberBinaryExpression
import expressions.PolynomialImplementation

interface Numeric : Expression {

    companion object Element : Numeric {
        override fun toString(): String = "element"

        override val toPolynomialImplementation =
            PolynomialImplementation(mutableListOf(0, 1))
    }

    val toPolynomialImplementation: PolynomialImplementation
}

data class ConstantExpression(val number: String) : Numeric {
    override fun toString(): String = number

    override val toPolynomialImplementation = PolynomialImplementation(mutableListOf(number.toInt()))
}

data class Plus(override val leftPart: Numeric, override val rightPart: Numeric, override val type: String = "+") : NumberBinaryExpression(leftPart, rightPart, "+"), Numeric {

    override fun toString(): String = NumberBinaryExpression(leftPart, rightPart, type).toString()
    override val toPolynomialImplementation: PolynomialImplementation
        get() = leftPart.toPolynomialImplementation.plus(rightPart.toPolynomialImplementation)
}

data class Minus(override val leftPart: Numeric, override val rightPart: Numeric) : NumberBinaryExpression(leftPart, rightPart, "-"), Numeric {
    override fun toString(): String = NumberBinaryExpression(leftPart, rightPart, "-").toString()

    override val toPolynomialImplementation: PolynomialImplementation
        get()  = leftPart.toPolynomialImplementation.minus(rightPart.toPolynomialImplementation)
}

data class Times(override val leftPart: Numeric, override val rightPart: Numeric) : NumberBinaryExpression(leftPart, rightPart, "*"), Numeric {
    override fun toString():String = NumberBinaryExpression(leftPart, rightPart, "*").toString()

    override val toPolynomialImplementation: PolynomialImplementation
        get()  = leftPart.toPolynomialImplementation.times(rightPart.toPolynomialImplementation)
}
