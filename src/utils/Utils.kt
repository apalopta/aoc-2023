import day01.Direction
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

fun readText(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

open class LinearGridBoard(private val rows: Int, private val columns: Int) {
    fun Field.distanceTo(other: Field): Int {
        val row = this / rows
        val column = this % columns
        val otherRow = other / rows
        val otherColumn = other % columns

        val distanceHorizontal = abs(row - otherRow)
        val distanceVertical = abs(column - otherColumn)
        return when {
            distanceHorizontal == 0 && distanceVertical == 1 -> 1
            distanceHorizontal == 1 && distanceVertical == 1 -> 1
            distanceHorizontal == 1 && distanceVertical == 0 -> 1
            distanceHorizontal == 0 && distanceVertical == 0 -> 0
            else -> 2
        }
    }

    fun Field.neighbours() {
        Direction.entries.filter {
            this.neighbour(it) != null
        }
    }

    fun Field.neighbour(direction: Direction): Field? {
        val row = this / rows
        val column = this % columns

        return when (direction) {
            Direction.EAST -> if (column + 1 < columns) this + 1 else null
            Direction.WEST -> if (column - 1 >= 0) this - 1 else null
            Direction.NORTH -> if (row - 1 >= 0) this - columns else null
            Direction.SOUTH -> if (row + 1 < rows) this + columns else null
        }
    }
}

fun IntArray.displayAsBoard(width: Int) {
    printlnDashed(width)
    toList().chunked(width).forEach { row ->
        println(row.joinToString(" "))
    }
    printlnDashed(width)
}

fun CharArray.displayAsBoard(width: Int) {
    printlnDashed(width)
    toList().chunked(width).forEach { row ->
        println(row.joinToString(" "))
    }
    printlnDashed(width)
}

private fun printlnDashed(width: Int) {
    println(Array(width) { "-" }.joinToString("-"))
}
