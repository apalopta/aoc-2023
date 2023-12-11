import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun emptyColumns(input: List<String>): MutableList<Int> {
        val emptyColumns = mutableListOf<Int>()
        for (i in input[0].indices) {
            if (input.map { it[i] }.none { it == '#' })
                emptyColumns.add(i)
        }
        return emptyColumns
    }

    fun emptyLines(input: List<String>): List<Int> = input
        .mapIndexed { i, line -> if (line.none { it == '#' }) i else 0 }
        .filter { it != 0 }

    fun expand(input: List<String>, emptyLineIndices: List<Int>, emptyColumns: MutableList<Int>): List<String> {
        val replacedLines = input.toMutableList()
        for (i in emptyLineIndices.reversed()) repeat(1) { replacedLines.add(i, ".".padEnd(input[0].length, '.')) }

        val expanded = replacedLines.map { line ->
            var newLine = line.toMutableList()
            for (i in emptyColumns.reversed())
                repeat(1) { newLine.add(i, '.') }
            newLine.joinToString("")
        }
        return expanded
    }

    fun galaxies(expanded: List<String>): MutableList<Galaxy> {
        val galaxies = mutableListOf<Galaxy>()
        var id: Int = 1
        for (i in expanded.indices) {
            for (j in expanded[i].indices) {
                if (expanded[i][j] == '#') {
                    galaxies.add(Galaxy(id, Pos(i, j)))
                    id++
                }
            }
        }
        return galaxies
    }

    fun pairs(galaxies: MutableList<Galaxy>): List<Pair<Galaxy, Galaxy>> {
        val pairs = galaxies.mapIndexed { i, g ->
            galaxies.subList(i + 1, galaxies.size).map { h ->
                Pair(g, h)
            }
        }.flatten()
        return pairs
    }

    fun part1(input: List<String>): Int {
        val emptyLines = emptyLines(input)
        val emptyColumns = emptyColumns(input)
        val expanded = expand(input, emptyLines, emptyColumns)
        val galaxies = galaxies(expanded)
        val pairs = pairs(galaxies)

        return pairs.sumOf { it.first.distance(it.second) }
    }

    fun part2(input: List<String>, n: Long): Long {
        val emptyLines = emptyLines(input)
        val emptyColumns = emptyColumns(input)
//        val expanded = expand(input, emptyLineIndices, emptyColumns)
        val galaxies = galaxies(input)
        val pairs = pairs(galaxies)

        return pairs.sumOf { it.first.expandedDistance(it.second, n, emptyLines, emptyColumns) }//.also { println(it) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day11/test-1")) == 374)
    check(part2(readInput("day11/test-1"), (2-1).toLong()
    ) == 374L)
//    check(part2(readInput("day11/test-1"), 9-1L) == 1030L)
    //

    val input = readInput("day11/input")
    part1(input).println()
    part2(input, 1000000-1).println()
}

data class Pos(val i: Int, val j: Int) {
    fun distance(other: Pos): Int {
        val dy = abs(other.i - i)
        val dx = abs(other.j - j)

        return if (j == other.j && i != other.i) {
            dy
        } else if (i == other.i && j != other.j) {
            dx
        } else {
            dx + dy
        }
    }

    fun expandedDistance(other: Pos, n: Long, expandedRows: List<Int>, expandedColumns: List<Int>): Long {
//        println("expandedRows: $expandedRows, expandedColumns: $expandedColumns")
        val rowRange = (min(i, other.i)..max(i, other.i))//.also { println("rowRange: $it") }
        val columnRange = (min(j, other.j)..max(j, other.j))//.also { println("columnRange: $it")}

        val numberOfRowExpands = rowRange.toList().intersect(expandedRows.toSet()).size//.also { println("row intersects: $it")}
        val numberOfColumnExpands = columnRange.toList().intersect(expandedColumns.toSet()).size//.also { println("column intersects: $it")}

        val dy = abs(other.i - i) + numberOfRowExpands * n
        val dx = abs(other.j - j) + numberOfColumnExpands * n

        return if (j == other.j && i != other.i) {
            dy
        } else if (i == other.i && j != other.j) {
            dx
        } else {
            dx + dy
        }
    }
}

data class Galaxy(val id: Int, val pos: Pos) {
    fun distance(other: Galaxy) = this.pos.distance(other.pos)
    fun expandedDistance(other: Galaxy, n: Long, expandedRows: List<Int>, expandedColumns: List<Int>) =
        this.pos.expandedDistance(other.pos, n, expandedRows, expandedColumns)
}