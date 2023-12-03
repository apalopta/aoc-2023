fun main() {

    fun part1(input: List<String>): Int = input.map { it.firstAndLastDigit() }.sumOf { it.toInt() }

    fun part2(input: List<String>): Int = input.map { it.firstAndLastNumber() }.sumOf { it.toInt() }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01-test-1")) == 142)
    check(part2(readInput("Day01-test-2")) == 281)

    val input = readInput("Day01-input")
    part1(input).println()
    part2(input).println()
}

fun String.firstAndLastDigit(): String = "${firstOrNull { it.isDigit() } ?: "0"}${lastOrNull { it.isDigit() } ?: "0"}"

private fun String.firstAndLastNumber(): String = "${this.firstDigit(Direction.FORWARD)}${this.firstDigit(Direction.BACKWARD)}"

private fun String.firstDigit(direction: Direction): String? {
    val indices = if (direction == Direction.FORWARD) this.indices else this.indices.reversed()
    for (i in indices) {
        if (this[i].isDigit()) return "${this[i]}"
        replacements.keys.firstOrNull() { k -> this.substring(i).startsWith(k) }?.let {
            return replacements[it]
        }
    }
    return "0"
}

enum class Direction {
    FORWARD,
    BACKWARD;
}

// =============================================================================
// This does work with the test data, but is wrong:
// "eighthree" in the end will be "8hree", but the correct result is produced only if we evaluate to "eigh3", i.e. from the end!
// unfortunately this is not clearly stated in the description
fun List<String>.replaceNumberStrings(replacements: Map<String, String>): List<String> {
    return this.map {
        it.replaceAllInString(replacements)
    }
}

private fun String.replaceAllInString(replacements: Map<String, String>): String {
    var string = this
    val sb = StringBuilder()
    while (string.length > 0) {
        val k = replacements.keys.find { k -> string.startsWith(k) }
        if (k != null) {
            string = string.removePrefix(k)
            sb.append(replacements[k])
        } else {
            sb.append(string[0])
            string = string.drop(1)
        }
    }
    return sb.toString()
}

val replacements = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)