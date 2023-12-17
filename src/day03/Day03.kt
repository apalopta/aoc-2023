package day03

import utils.println
import utils.readInput
import utils.LinearGridBoard
import kotlin.time.measureTime

typealias EnginePositions = CharArray

fun main() {
    fun part1(input: List<String>): Int {
        with(Engine.from(input)) {
            return getGearNumbers { numbers ->
                numbers
                    .map { it.sum() }
            }.sum()
        }
    }

    fun part2(input: List<String>): Int {
        with(Engine.from(input)) {
            return getGearNumbers { numbers ->
                numbers
                    .filter { it.size == 2 }
                    .map { it[0] * it[1] }
            }.sum()
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day03/test-1")) == 4361)
    check(part2(readInput("day03/test-1")) == 467835)

    val input = readInput("day03/input")

    val time1 = measureTime { part1(input).println() }
    println(time1.inWholeMilliseconds)
    val time2 = measureTime { part2(input).println() }
    println(time2.inWholeMilliseconds)
}

fun Engine.getGearNumbers(c: (List<List<Int>>) -> List<Int>): List<Int> {
    val rangesToNumbers = getNumberRangesToNumbers()
    return gears.flatMap { gear ->
        val res = engineParts.mapIndexed { index, part ->
            if (part == gear)
                rangesToNumbers
                    .filter { (indices, _) -> indices.any { it.distanceTo(index) == 1 } }
                    .map { it.value }
            else
                listOf(0)
        }
        c(res)
    }
}

fun Engine.getNumberRangesToNumbers(): Map<IntRange, Int> {

    return findNumbersInRows()

    // return findNumbersInString()
}

// finding numbers while iterating over rows - it's faster
private fun Engine.findNumbersInRows(): Map<IntRange, Int> {
    val rows = data.toList().chunked(width)
    val map = mutableMapOf<IntRange, Int>()

    var currentIndex = 0
    rows.forEachIndexed { _, row ->
        val chars = mutableListOf<Char>()
        var startAt = 0
        for ((i, char) in row.withIndex()) {
            if (char.isDigit()) {
                if (chars.isEmpty()) startAt = currentIndex
                chars.add(char)
            } else if (!char.isDigit() || i == (width - 1)) {
                if (chars.isNotEmpty()) {
                    map[(startAt until currentIndex)] = chars.joinToString("").toInt()
                    chars.removeAll { true }
                    startAt = 0
                }
            }
            currentIndex++
        }
    }

    return map
}

// alternative that reduces a string
private fun Engine.findNumbersInString(): Map<IntRange, Int> {
    val replaced = data.joinToString("").replace("""[\D+]""".toRegex(), ".")
    var string = replaced.dropWhile { it == '.' }
    var currentIndex = replaced.length - string.length

    val numbers = mutableMapOf<IntRange, Int>()
    while (string.isNotEmpty()) {
        val candidate = string.substringBefore(".")

        numbers[(currentIndex until (currentIndex + candidate.length))] = candidate.toInt()

        string = string.removePrefix(candidate)
        currentIndex += candidate.length
        val newString = string.dropWhile { !it.isDigit() }
        currentIndex += (string.length - newString.length)
        string = newString
    }
    return numbers
}

class Engine(val data: CharArray, val width: Int, val height: Int) : LinearGridBoard(width, height) {
    val engineParts = EnginePositions(width * height) {
        if (data[it].isDigit()) '.' else (if (data[it] != '.') data[it] else '.')
    }
    val gears = engineParts.filter { it != '.' }.distinct()

    companion object {
        fun from(input: List<String>): Engine {
            require(input.map { it.length }.distinct().size == 1) { "expect all lines are of same length" }
            val width = input[0].length
            val height = input.size
            return Engine(input.joinToString("").toCharArray(), width, height)
        }
    }
}
