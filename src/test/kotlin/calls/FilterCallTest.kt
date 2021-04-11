package calls

import expressions.bool.Bool
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import parser.parseCall

internal class FilterCallTest {

    @Test
    fun testToString() {
        val call = parseCall("filter{(element>100)}")

        assertEquals(call.toString(), "filter{(element>100)}")
    }
}