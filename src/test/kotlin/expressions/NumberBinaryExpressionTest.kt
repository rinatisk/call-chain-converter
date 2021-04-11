package expressions

import expressions.num.ConstantExpression
import expressions.num.Numeric
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NumberBinaryExpressionTest {

    @Test
    fun testToStringNum() {
        val a = NumberBinaryExpression(Numeric.Element, ConstantExpression("10"), "+").toString()

        assertEquals("(element+10)", a)
    }
    @Test
    fun testToStringBool() {
        val a = NumberBinaryExpression(Numeric.Element, ConstantExpression("10"), "=").toString()

        assertEquals("(element=10)", a)
    }
}