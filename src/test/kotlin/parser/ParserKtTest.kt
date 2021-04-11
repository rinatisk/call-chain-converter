package parser

import calls.Call
import calls.CallChain
import calls.FilterCall
import calls.MapCall
import expressions.bool.Greater
import expressions.num.ConstantExpression
import expressions.num.Numeric
import expressions.num.Plus
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ParserKtTest {

    @Test
    fun parseCallChain() {
        val file = javaClass.getResource("CallChain.txt").file
        val a = parseCallChain(File(file).readText())

        val b = CallChain(listOf<Call>(MapCall(Plus(Numeric.Element, ConstantExpression("10"))), FilterCall(Greater(Numeric.Element, ConstantExpression("10")))))

        assertEquals(b, a)
    }

    @Test
    fun parseCall() {
        val file = javaClass.getResource("Call.txt").file
        val a = parseCall(File(file).readText())

        val b = FilterCall(Greater(Numeric.Element, ConstantExpression("10")))

        assertEquals(b, a)
    }

    @Test
    fun isConstantExpression() {
        val a = "10"

        assertTrue(isConstantExpression(a))
    }

    @Test
    fun parseConstantExpression() {
        val a = "-10"

        assertEquals(ConstantExpression("-10"), parseConstantExpression(a))
    }

}