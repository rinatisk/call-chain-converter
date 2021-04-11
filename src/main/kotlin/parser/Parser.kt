package parser

import calls.*
import exceptions.ParseException
import exceptions.TypeException
import expressions.*
import expressions.bool.*
import expressions.num.*

fun parseCallChain(callChain: String): CallChain = CallChain(callChain.split("%>%").map { parseCall(it) })

fun parseCall(call: String): Call = when {
    call.startsWith("map{") && call.endsWith("}") -> {
        try {
            MapCall(parseExpression(call.removePrefix("map{").removeSuffix("}")) as Numeric)
        } catch (e: ParseException) {
            throw TypeException()
        }
    }
    call.startsWith("filter{") && call.endsWith("}") -> {
        try {
            FilterCall(parseExpression(call.removePrefix("filter{").removeSuffix("}")) as Bool)
        } catch (e: ParseException) {
            throw TypeException()
        }
    }
    else -> throw ParseException()
}

fun parseExpression(expression: String): Expression {
    if (expression == "element") {
        return Numeric.Element
    }
    return if (isConstantExpression(expression)) parseConstantExpression(expression) else parseBinaryOperation(expression)
}

fun isConstantExpression(expression: String) =
    Regex("-?\\d+").matches(expression)

fun parseConstantExpression(expression: String): ConstantExpression =
    if (isConstantExpression(expression)) ConstantExpression(expression) else throw ParseException()

fun parseBinaryOperation(expression: String): BinaryExpression {
    val operatorSign = "[-<>=&|*+]"
    val constantExpression = Regex("[-+]?\\d+")
    val twoConstants = Regex("\\($constantExpression$operatorSign$constantExpression\\)")
    val leftPart = Regex("\\(element($operatorSign)(.*)\\)")
    val rightElem = Regex("\\(.*($operatorSign)element\\)")
    val twoOperations = Regex("\\(\\(.*\\)($operatorSign)\\(.*\\)\\)")
    val rightConstant = Regex("\\(\\(.*\\)$operatorSign$constantExpression\\)")
    val leftConstant = Regex("\\($constantExpression$operatorSign\\(.*\\)\\)")

    if (leftPart.matchEntire(expression) != null) {
        val (operator, rightPart) = leftPart.matchEntire(expression)!!.destructured

        return newOperator(operator, Numeric.Element, parseExpression(rightPart))
    }


    if (rightElem.matchEntire(expression) != null) {
        val (leftExp, operator) = rightElem.matchEntire(expression)!!.destructured

        return newOperator(operator, parseExpression(leftExp), Numeric.Element)
    }

    if (twoOperations.matchEntire(expression) != null) {
        val (leftExp, operator, rightPart) = twoOperations.matchEntire(expression)!!.destructured

        return newOperator(operator, parseExpression(leftExp), parseExpression(rightPart))
    }


    if (rightConstant.matchEntire(expression) != null) {
        val (leftExp, operator, rightPart) = rightConstant.matchEntire(expression)!!.destructured

        return newOperator(operator, parseExpression(leftExp), ConstantExpression(rightPart))
    }


    if ( leftConstant.matchEntire(expression) != null) {

        val (leftExp, operator, rightPart) =  leftConstant.matchEntire(expression)!!.destructured

        return newOperator(operator, ConstantExpression(leftExp), parseExpression(rightPart))
    }


    if (twoConstants.matchEntire(expression) != null) {
        val (leftExp, operator, rightPart) = twoConstants.matchEntire(expression)!!.destructured

        return newOperator(operator, parseExpression(leftExp), parseExpression(rightPart))

    } else throw ParseException()
}
