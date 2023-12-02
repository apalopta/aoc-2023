fun main() {
    fun part1(input: List<String>): Int {
        return 1
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("DayXX-test-1")) == 1)
    check(part2(readInput("DayXX-test-2")) == 1)

    val input = readInput("DayXX-input")
    part1(input).println()
    part2(input).println()
}
