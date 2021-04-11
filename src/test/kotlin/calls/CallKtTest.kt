package calls

import exceptions.ParseException
import exceptions.TypeException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import parser.parseCallChain
import java.io.File

internal class CallKtTest {

    companion object {
        @JvmStatic
        fun inputData(): List<Arguments> = listOf(
            Arguments.of("bigCallExpected.txt", "bigCall.txt"),
            Arguments.of("smallCallExpected.txt", "smallCall.txt"),
            Arguments.of("usualCallExpected.txt", "usualCall.txt"),
            Arguments.of("callWithSimplifyExpected.txt", "callWithSimplify.txt")
        )
    }

    @MethodSource("inputData")
    @ParameterizedTest(name = "inputData {index}, {1}")
    fun convert(expected: String, resource: String) {
        val fileActual = javaClass.getResource(resource).file
        val fileExpected = javaClass.getResource(expected).file

        val textActual = File(fileActual).readText()
        val actual = parseCallChain(textActual).convert()

        val textExpected = File(fileExpected).readText()

        assertEquals(textExpected, actual)

    }

    @Test
    fun checkThrowsType() {
        val fileActual = javaClass.getResource("parseExc.txt").file

        val textActual = File(fileActual).readText()

        assertThrows(ParseException::class.java) {
            parseCallChain(textActual)
        }
    }

    @Test
    fun checkThrowsParse() {
        val fileActual = javaClass.getResource("typeExc.txt").file

        val textActual = File(fileActual).readText()

        assertThrows(TypeException::class.java) {
            parseCallChain(textActual)
        }

    }
}
