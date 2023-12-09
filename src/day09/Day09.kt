typealias ReducedSequence = List<List<Long>>

fun main() {
    fun List<Long>.toReducedSequence(): ReducedSequence {
        val sequences = mutableListOf(this)
        while (sequences.last().any { it != 0L }) {
            sequences.add(sequences.last().zipWithNext { n, next -> next - n })
        }
        return sequences
    }

    fun String.toLongs() = split(" ").map { n -> n.toLong() }

    fun String.toReducedSequence() = toLongs().toReducedSequence()

    fun part1(input: List<String>): Long = input
        .sumOf { line ->
            line.toReducedSequence()
                .map { it.last() }
                .reversed()
                .reduce { acc, next -> acc + next }
        }

    fun part2(input: List<String>): Long = input
        .sumOf { line ->
            line.toReducedSequence()
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


