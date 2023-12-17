import utils.println
import utils.readInput

fun main() {

    fun CharArray.hash15(): Int = toList().fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    fun String.hash15(): Int = toCharArray().hash15()

    fun part1(input: List<String>): Int = input.sumOf { it.hash15() }

    fun List<String>.steps(): List<Step> {
        val steps: List<Step> = map { string ->
            if (string.contains('=')) {
                val label = string.substringBefore('=')
                val focalLength = string.substringAfter('=').toInt()
                AddStep(label.hash15(), label, Lens(label, focalLength))
            } else {
                val label = string.substringBefore('-')
                RemoveStep(string.substringBefore('-').hash15(), label)
            }
        }
        return steps
    }

    fun List<Step>.boxes(): Array<MutableList<Lens>> {
        val boxes = Array<MutableList<Lens>>(256) { mutableListOf() }
        forEach { step ->
            val box = boxes[step.hash]
            val i = box.indexOfFirst { it.label == step.label }
            when (step) {
                is AddStep -> if (i != -1) box[i] = step.lens else box.add(step.lens)
                is RemoveStep -> if (i != -1) box.removeAt(i)
                else -> error("unexpected Step '$step'")
            }
        }
        return boxes
    }

    fun part2(input: List<String>): Int = input
        .steps()
        .boxes()
        .mapIndexed { index: Int, box: MutableList<Lens> ->
            (index + 1) * (box.mapIndexed { i, lens ->
                (i + 1) * lens.focalLength
            }.sum())
        }.sum()

    fun readAsJoinedTextSplitBy(path: String, char: Char = ',') = readInput(path).joinToString("").split(char)

    // test if implementation meets criteria from the description, like:
    check(part1(readAsJoinedTextSplitBy("day15/test-1")) == 1320)
    check(part2(readAsJoinedTextSplitBy("day15/test-1")) == 145)

    val input = readAsJoinedTextSplitBy("day15/input")
    part1(input).println()
    part2(input).println()
}

sealed class Step(val hash: Int, val label: String)
class RemoveStep(hash: Int, label: String) : Step(hash, label)
class AddStep(hash: Int, label: String, val lens: Lens) : Step(hash, label)

data class Lens(val label: String, val focalLength: Int)
