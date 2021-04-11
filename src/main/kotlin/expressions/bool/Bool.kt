package expressions.bool

import expressions.BinaryExpression
import expressions.Expression
import expressions.PolynomialImplementation
import expressions.bool.Equals.Companion.FALSE
import expressions.bool.Equals.Companion.TRUE
import expressions.num.ConstantExpression
import expressions.num.Minus
import expressions.num.Numeric

interface Bool : Expression {
    fun simplify(): Expression
}

abstract class BoolBinaryExpression(override val leftPart: Expression, override val rightPart: Expression, override val type: String) :
    BinaryExpression(leftPart, rightPart, type) {
    override fun toString(): String = "($leftPart$type$rightPart)"
    abstract fun simplify(): Bool
}

class Greater(override val leftPart: Numeric, override val rightPart: Numeric, override val type: String = ">") : BoolBinaryExpression(leftPart, rightPart, ">"), Bool {
    override fun toString(): String = "($leftPart$type$rightPart)"

    override fun simplify(): Bool = GreaterZeroPolynomial(PolynomialHelper(leftPart, rightPart).polynomial).simplify()
}

class PolynomialHelper(val leftNum: Numeric, val rightNum: Numeric) {

    val polynomial: PolynomialImplementation
        get() = Minus(leftNum, rightNum).toPolynomialImplementation

}

class GreaterZeroPolynomial(val polynomial: PolynomialImplementation) : Bool {

    override fun simplify(): Bool = when (polynomial.degree) {
            -1 -> FALSE
            0 -> if (polynomial.coefficientList[0] > 0) TRUE else FALSE
            else -> this
    }

    fun compositionOr(toCompose: GreaterZeroPolynomial): Bool = when {
            this.polynomial.equals(toCompose.polynomial) -> this
            else -> Or(this, toCompose)
    }

    fun compositionAnd(toCompose: GreaterZeroPolynomial): Bool = when {
            this.polynomial.equals(toCompose.polynomial) ->  this
            else -> And(this, toCompose)
    }

    override fun toString(): String {
        return "($polynomial>0)"
    }

}
class Less(override val leftPart: Numeric, override val rightPart: Numeric) : BoolBinaryExpression(leftPart, rightPart, "<"), Bool {
    override fun toString(): String = "($leftPart$type$rightPart)"

    override fun simplify(): Bool = GreaterZeroPolynomial(PolynomialHelper(rightPart, leftPart).polynomial).simplify()
}

class Equals(override val leftPart: Numeric, override val rightPart: Numeric, val booleanValue: Boolean? = null) : BoolBinaryExpression(leftPart, rightPart, "="), Bool {
    companion object {


        val TRUE = Equals(ConstantExpression("1"), ConstantExpression("1"), true)

        val FALSE = Equals(ConstantExpression("1"), ConstantExpression("0"), false)
    }

    override fun toString(): String = "($leftPart=$rightPart)"

    override fun simplify(): Bool = EqualsZeroPolynomial(PolynomialHelper(leftPart, rightPart).polynomial).simplify()
}
class EqualsZeroPolynomial(val polynomial: PolynomialImplementation) : Bool {

    override fun simplify(): Bool = when (polynomial.degree) {
            -1 -> TRUE
            0 -> if (polynomial.coefficientList.first() == 0) TRUE else FALSE
            else -> this
    }

    fun compositionOr(toCompose: EqualsZeroPolynomial): Bool =
        if (this.polynomial.equals(toCompose.polynomial)) this
        else Or(this, toCompose)

    fun compositionAnd(toCompose: EqualsZeroPolynomial): Bool =
        if (this.polynomial.equals(toCompose.polynomial)) this
        else And(this, toCompose)


    override fun toString(): String = "($polynomial=0)"
}

class And(override val leftPart: Bool, override val rightPart: Bool) : BoolBinaryExpression(leftPart, rightPart, "&"), Bool {
    override fun toString(): String = "($leftPart$type$rightPart)"

    override fun simplify(): Bool {
        val leftPart = leftPart.simplify() as Bool
        val rightPart = rightPart.simplify() as Bool
        return when {
            (leftPart is Equals && leftPart.booleanValue != null && rightPart is Equals && rightPart.booleanValue != null) -> {
               if (leftPart.booleanValue.and(rightPart.booleanValue)) TRUE else FALSE
            }
            leftPart is GreaterZeroPolynomial && rightPart is GreaterZeroPolynomial -> {
                leftPart.compositionAnd(rightPart)
            }
            leftPart is EqualsZeroPolynomial && rightPart is EqualsZeroPolynomial -> {
                leftPart.compositionAnd(rightPart)
            }
            else -> And(leftPart, rightPart)
        }
    }
}

class Or(override val leftPart: Bool, override val rightPart: Bool) : BoolBinaryExpression(leftPart, rightPart, "|"), Bool {
    override fun toString(): String = "($leftPart$type$rightPart)"

    override fun simplify(): Bool {
        val leftPart = leftPart.simplify() as Bool
        val rightPart = rightPart.simplify() as Bool
        return when {
            (leftPart is Equals && leftPart.booleanValue != null && rightPart is Equals && rightPart.booleanValue != null) -> {
                if (leftPart.booleanValue.or(rightPart.booleanValue)) TRUE else FALSE
            }
            leftPart is GreaterZeroPolynomial && rightPart is GreaterZeroPolynomial -> {
                leftPart.compositionOr(rightPart)
            }
            leftPart is EqualsZeroPolynomial && rightPart is EqualsZeroPolynomial -> {
                leftPart.compositionOr(rightPart)
            }
            else -> Or(leftPart, rightPart)
        }
    }
}
