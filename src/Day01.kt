fun main() {
    fun sumUp(input: List<String>): Int = input
        .map { it.replace("[a-z]".toRegex(), "") }
        .map {if (it.length == 0) "0" else "${it.first()}${it.last()}" }
        .map { it.toInt() }
        .sum()

    fun part1(input: List<String>): Int {
        return sumUp(input)
    }

    fun part2(input: List<String>): Int {
        return sumUp(input.replaceNumberStrings(replacements))
    }

    // test if implementation meets criteria from the description, like:
    check(part2(readInput("Day01-test-1")) == 142)
    check(part2(readInput("Day01-test-2")) == 281)

    val input = readInput("Day01-input")
    part1(input).println()
    part2(input).println()
}

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