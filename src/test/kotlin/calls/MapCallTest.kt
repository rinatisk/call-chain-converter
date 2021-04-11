package calls

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import parser.parseCall

internal class MapCallTest {

    @Test
    fun testToString() {
        val call = parseCall("map{(element*5)}")

        assertEquals(call.toString(), "map{(element*5)}")
    }
}