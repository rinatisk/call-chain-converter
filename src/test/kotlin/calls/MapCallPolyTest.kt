package calls

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import parser.parseCall

internal class MapCallPolyTest {

    @Test
    fun testToString() {
        val call = parseCall("map{(element*element)}")

        assertEquals(call.toString(), "map{(element*element)}")
    }
}