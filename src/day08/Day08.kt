import kotlin.system.measureTimeMillis
import kotlin.time.measureTimedValue

@JvmInline
value class Id(val id: String)

data class Instruction(val id: Id, val left: Id, val right: Id) {
    fun next(direction: Char) = when (direction) {
        'R' -> right
        'L' -> left
        else -> error("wrong input '$direction'")
    }
}

fun main() {
    fun instructionMap(input: List<String>): Map<Id, Instruction> {
        val mapping = input.drop(2).map { line ->
            val id = line.substringBefore("=").trim()
            val left = line.substringAfter("(").substringBefore(",")
            val right = line.substringAfter(",").substringBefore(")").trim()
            Instruction(Id(id), Id(left), Id(right))
        }.associateBy { it.id }
        return mapping
    }

    fun part1(input: List<String>): Long {
        val instructionSet = input[0].toCharArray()
        val mapping = instructionMap(input)

        var current = mapping.keys.find { it == Id("AAA") }
        var steps = 0L

        outer@ while (true) {
            for (dir in instructionSet) {
                if (current == Id("ZZZ")) break@outer
                current = mapping[current]?.next(dir) ?: error("we are in trouble - no instruction found")
                steps++
            }
        }

        return steps
    }

    fun part2(input: List<String>): Long {
        val instructionSet = input[0].toCharArray()
        val mapping = instructionMap(input)

        var currents = mapping.keys.filter { it.id.endsWith('A') }
        currents.forEach { println(it) }
        var steps = 0L

        outer@ while (true) {
            for (dir in instructionSet) {
                // doesn't work for huge instruction sets
                if (currents.all { it.id.endsWith('Z')}) break@outer
                currents = currents.map { mapping[it]?.next(dir) ?: error("we are in trouble") }
                steps++
            }
        }

        return steps
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day08/test-1")) == 6L)
    check(part1(readInput("day08/test-2")) == 2L)
    check(part2(readInput("day08/test-3")) == 6L)

    val input = readInput("day08/input")
    part1(input).println()
    val time = measureTimedValue {
        part2(input).println()
    }
    println("took: ${time.duration.inWholeSeconds}")
}
