import kotlin.math.abs

typealias Field = Int
typealias EnginePositions = CharArray

fun main() {
    fun part1(input: List<String>): Int {
        val width = input[0].length
        val height = input.size
        val engine = Engine(input.joinToString("").toCharArray(), width, height)

//        engine.engineParts.displayAsBoard(width, height)
//        engine.digitPositions.displayAsBoard(width, height)
        with(LinearGridBoard(width, height)) {
            return doIt(engine, input.joinToString("")) { lists ->
                lists.map { it.sum() } }
                .sum()
        }
    }

    fun part2(input: List<String>): Int {
        val width = input[0].length
        val height = input.size
        val engine = Engine(input.joinToString("").toCharArray(), width, height)

//        engine.engineParts.displayAsBoard(width, height)
//        engine.digitPositions.displayAsBoard(width, height)
        with(LinearGridBoard(width, height)) {
            return doIt(engine, input.joinToString("")) { lists ->
                lists.filter { it.size == 2 }
                    .map { it[0] * it[1] }
            }.sum()
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day03/test-1")) == 4361)
    check(part2(readInput("day03/test-1")) == 467835)

    val input = readInput("day03/input")
    part1(input).println()
    part2(input).println()
}

context(LinearGridBoard)
fun doIt(engine: Engine, input: String, c: (List<List<Int>>) -> List<Int>): List<Int> {
    val rangesToNumbers = engine.getNumbers(input)
    return engine.gears.flatMap { gear ->
        val res = engine.engineParts.mapIndexed { index, part ->
            if (part == gear)
                rangesToNumbers
                    .filter { (indices, value) -> indices.any { it.distanceTo(index) == 1 } }
                    .map { it.value }
            else
                listOf(0)
        }
        c(res)
    }
}


class Engine(val data: CharArray, width: Int, height: Int) : LinearGridBoard(width, height) {
    val engineParts = EnginePositions(width * height) {
        if (data[it].isDigit()) '.' else (if (data[it] != '.') data[it] else '.')
    }
    val gears = engineParts.filter { it != '.' }.distinct()

    fun getNumbers(data: String): Map<IntRange, Int> {
        val replaced = data.replace("""[\D+]""".toRegex(), ".")
        val numbers = mutableMapOf<IntRange, Int>()
        var string = replaced.dropWhile { it == '.' }
        var currentIndex = replaced.length - string.length

        while (string.isNotEmpty()) {
            val candidate = string.substringBefore(".")

            numbers[(currentIndex until currentIndex + candidate.length)] = candidate.toInt()

            string = string.removePrefix(candidate)
            currentIndex += candidate.length
            val newString = string.dropWhile { !it.isDigit() }
            currentIndex += (string.length - newString.length)
            string = newString
        }

        return numbers
    }
}

open class LinearGridBoard(private val rows: Int, private val columns: Int) {

    fun Field.distanceTo(other: Field): Int {
        val row = this / rows
        val column = this % columns
        val otherRow = other / rows
        val otherColumn = other % columns

        val distanceHorizontal = abs(row - otherRow)
        val distanceVertical = abs(column - otherColumn)
        return when {
            distanceHorizontal == 0 && distanceVertical == 1 -> 1
            distanceHorizontal == 1 && distanceVertical == 1 -> 1
            distanceHorizontal == 1 && distanceVertical == 0 -> 1
            distanceHorizontal == 0 && distanceVertical == 0 -> 0
            else -> 2
        }
    }
}

fun IntArray.displayAsBoard(width: Int, height: Int) {
    println(Array(width) { "-" }.joinToString("-"))
    toList().chunked(width).forEach { row ->
        println(row.joinToString(" "))
    }
    println(Array(width) { "-" }.joinToString("-"))
}

fun CharArray.displayAsBoard(width: Int, height: Int) {
    println(Array(width) { "-" }.joinToString("-"))
    toList().chunked(width).forEach { row ->
        println(row.joinToString(" "))
    }
    println(Array(width) { "-" }.joinToString("-"))
}
