package expressions

import expressions.bool.GreaterZeroPolynomial
import expressions.num.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ExpressionKtTest {

    @Test
    fun newBoolOperator() {
        val a = GreaterZeroPolynomial(PolynomialImplementation(mutableListOf(1, 2, 3)))
        val b = GreaterZeroPolynomial(PolynomialImplementation(mutableListOf(3, 2, 1)))
        val new = newOperator("|", a, b).toString()
        assertEquals(new, "((1+(2*element)+(3*element*element)>0)|(3+(2*element)+element*element>0))")

    }

    @Test
    fun newNumOperator() {
        val a = Plus(Numeric.Element, ConstantExpression("2"))
        val b = Minus(Numeric.Element, ConstantExpression("1"))
        val new = newOperator("+", a, b).toString()
        assertEquals(new, "((element+2)+(element-1))")
    }

    @Test
    fun composition() {
        val a = Plus(Numeric.Element, ConstantExpression("2"))
        val b = Times(Numeric.Element, Numeric.Element)
        val c = b.composition(a).toString()
        assertEquals(c, "((element+2)*(element+2))")

    }
}