package day09

import println
import readInput

fun main() {
    fun reduceLine(lineAsLongs: List<Long>): List<List<Long>> {
        val sequences = mutableListOf(lineAsLongs)
        while (sequences.last().any { it != 0L }) {
            sequences.add(sequences.last().zipWithNext { n0, n1 -> n1 - n0 })
        }
        return sequences
    }

    fun part1(input: List<String>): Long = input.sumOf { line ->
        val lineAsLongs = line.split(" ").map { n -> n.toLong() }
        reduceLine(lineAsLongs)
            .map { it.last() }
            .reversed()
            .reduce { acc, next -> acc + next }
    }

    fun part2(input: List<String>): Long = input.sumOf { line ->
        val lineAsLongs = line.split(" ").map { n -> n.toLong() }
        reduceLine(lineAsLongs)
            .map { it.first() }
            .reversed()
            .reduce { acc, next -> next - acc }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day09/test-1")) == 114L)
    check(part2(readInput("day09/test-1")) == 2L)

    val input = readInput("day09/input")
    part1(input).println()
    part2(input).println()
}


