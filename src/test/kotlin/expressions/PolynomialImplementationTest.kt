package expressions

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PolynomialImplementationTest {

    @Test
    fun getDegree() {
        val a = PolynomialImplementation(mutableListOf(1, 2, 3))

        assertEquals(2, a.degree)
    }

    @Test
    fun testEquals() {
        val a = PolynomialImplementation(mutableListOf(1, 2, 3))
        val b = PolynomialImplementation(mutableListOf(1, 2, 3, 4))
        val c = PolynomialImplementation(mutableListOf(1, 2, 3))

        assertTrue(a.equals(c))
        assertFalse(a.equals(b))
    }

    @Test
    fun plus() {
        val a = PolynomialImplementation(mutableListOf(1, 2, 3))
        val b = PolynomialImplementation(mutableListOf(3, 2, 1))

        val c = a.plus(b)

        assertEquals(listOf(4, 4, 4), c.coefficientList)
    }

    @Test
    fun minus() {
        val b = PolynomialImplementation(mutableListOf(1, 2, 3))
        val a = PolynomialImplementation(mutableListOf(3, 2, 5))

        val c = a.minus(b)

        assertEquals(listOf(2, 0, 2), c.coefficientList)
    }

    @Test
    fun times() {
        val a = PolynomialImplementation(mutableListOf(1, 2, 3))
        val b = PolynomialImplementation(mutableListOf(3, 2, 1))

        val c = a.times(b)

        assertEquals(listOf(3, 8, 14, 8, 3), c.coefficientList)
    }

    @Test
    fun testToString() {
        val a = PolynomialImplementation(mutableListOf(1, 2, 3))

        val c = a.toString()

        assertEquals("1+(2*element)+(3*element*element)", c)
    }
}