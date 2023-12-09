
private typealias ReducedSequence = List<List<Int>>

fun main() {
    // val sequences = mutableListOf(this)
    // while (sequences.last().any { it != 0L }) {
    //     sequences.add(sequences.last().zipWithNext { n, next -> next - n })
    // }
    // thanks to Olaf :-) - more elegant
    fun List<Int>.toReducedSequence(): ReducedSequence = generateSequence(this) { sequence ->
            sequence.zipWithNext { a, b -> b - a }
        }.takeWhile { s -> s.any { it != 0 } }.toList()

    fun String.toInts() = split(" ").map { n -> n.toInt() }

    fun String.toReducedSequence() = toInts().toReducedSequence()

    fun part1(input: List<String>): Int = input
        .sumOf { line ->
            line.toReducedSequence()
                .map { it.last() }
                .reversed()
                .reduce { acc, next -> acc + next }
        }

    fun part2(input: List<String>): Int = input
        .sumOf { line ->
            line.toReducedSequence()
                .map { it.first() }
                .reversed()
                .reduce { acc, next -> next - acc }
        }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day09/test-1")) == 114)
    check(part2(readInput("day09/test-1")) == 2)

    val input = readInput("day09/input")
    part1(input).println()
    part2(input).println()
}


