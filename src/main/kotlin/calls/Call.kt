package calls

import expressions.*
import expressions.bool.And
import expressions.bool.Bool
import expressions.bool.Equals
import expressions.num.Numeric

interface Call {
    val type: String
}

data class MapCall(val expression: Numeric, override val type: String = "map") : Call {
    override fun toString(): String = "$type{$expression}"
}

data class MapCallPolynomial(val polynomialImplementation: PolynomialImplementation, override val type: String = "map") :
    Call {
    override fun toString(): String = "$type{$polynomialImplementation}"
}

data class FilterCall(val expression: Bool, override val type: String = "filter") : Call {
    override fun toString(): String = "$type{$expression}"
}

data class CallChain(val calls: List<Call>)

fun CallChain.convert(): String {

    val mapCalls = calls.filterIsInstance<MapCall>()


    val mapCompositions = mutableListOf(mapCalls.first().expression)
    mapCalls.drop(1).map { mapCompositions.add(it.expression.composition(mapCompositions.last()) as Numeric) }

    var currentMap: Int? = null
    var filterExpression: Bool? = null
    calls.map {
        if (it is FilterCall) {
            if (filterExpression == null) {
                if (currentMap != null) filterExpression = it.expression.composition(mapCompositions[currentMap!!]) as Bool else it.expression
            }
            if (filterExpression != null) {
                if (currentMap != null) filterExpression = And(filterExpression!!, it.expression.composition(mapCompositions[currentMap!!]) as Bool) else
                    And(filterExpression!!, it.expression)
            }
        } else currentMap = currentMap?.plus(1) ?: 0
    }

    return listOf(FilterCall(filterExpression?.simplify() as Bool? ?: Equals.TRUE),
        when (mapCompositions.lastOrNull()) {
            is Numeric -> MapCallPolynomial(mapCompositions.last().toPolynomialImplementation)
            else -> MapCall(Numeric.Element)
        }).joinToString("%>%")

}
