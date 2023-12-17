package day11

import utils.println
import utils.readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun emptyColumns(input: List<String>): List<Int> = input[0].indices
        .mapNotNull { j ->
            if (input.map { line -> line[j] }.none { it == '#' }) j else null
        }

    fun emptyLines(input: List<String>): List<Int> = input
        .mapIndexedNotNull { i, line -> if (line.none { it == '#' }) i else null }

    fun List<String>.galaxies(): List<Galaxy> =
        flatMapIndexed { i, line ->
            line.indices.filter { j -> line[j] == '#' }
                .map { j -> Galaxy(i + j, Pos(i, j)) }
        }

    fun List<Galaxy>.toPairs(): List<Pair<Galaxy, Galaxy>> =
        mapIndexed { i, a ->
            subList(i + 1, size)
                .map { b -> Pair(a, b) }
        }.flatten()

    fun sumGalaxyDistances(input: List<String>, n: Int): Long {
        val emptyLines = emptyLines(input)
        val emptyColumns = emptyColumns(input)

        return input
            .galaxies()
            .toPairs()
            .sumOf { it.first.veryLongDistanceTo(it.second, n - 1, emptyLines, emptyColumns) }
    }

    // double empty rows and columns
    check(sumGalaxyDistances(readInput("day11/test-1"), 2) == 374L)

    val input = readInput("day11/input")
    sumGalaxyDistances(input, 2).println()
    sumGalaxyDistances(input, 1000000).println()
}

data class Pos(val i: Int, val j: Int) {
    fun simpleDistanceTo(other: Pos): Int {
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

    fun veryLongDistanceTo(other: Pos, n: Int, expandedRows: List<Int>, expandedColumns: List<Int>): Long {
        val rowRange = (min(i, other.i)..max(i, other.i))
        val columnRange = (min(j, other.j)..max(j, other.j))

        val numberOfRowExpands = rowRange.toList().intersect(expandedRows.toSet()).size
        val numberOfColumnExpands = columnRange.toList().intersect(expandedColumns.toSet()).size

        val dy = abs(other.i - i) + numberOfRowExpands * n.toLong()
        val dx = abs(other.j - j) + numberOfColumnExpands * n.toLong()

        return if (j == other.j && i != other.i) {
            dy
        } else if (i == other.i && j != other.j) {
            dx
        } else {
            dx + dy
        }
    }
}

data class Galaxy(val id: Int, val pos: Pos)

fun Galaxy.veryLongDistanceTo(other: Galaxy, n: Int, expandedRows: List<Int> = listOf(), expandedColumns: List<Int> = listOf()) =
    this.pos.veryLongDistanceTo(other.pos, n, expandedRows, expandedColumns)