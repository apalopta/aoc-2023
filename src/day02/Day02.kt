fun main() {
    fun part1(input: List<String>, otherGameSet: GameSet): Int = input
        .readGames()
        .filter { game: Game -> game.sets.all { it <= otherGameSet } }
        .sumOf { it.id }

    fun part2(input: List<String>): Int = input
        .readGames()
        .sumOf { game -> game.minimumSet().power() }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day02/test-1"), GameSet(12, 13, 14)) == 8)
    check(part2(readInput("day02/test-1")) == 2286)

    val input = readInput("day02/input")
    part2(input).println()
}

fun List<String>.readGames(): List<Game> {
    return this.map { gameLine ->
        val id = gameLine.substringBefore(":").substringAfter("Game ").toInt()
        val sets = gameLine.substringAfter(":").split(";").map {
            it.split(",").map { it.trim() }.toGameSet()
        }
        Game(id, sets)
    }
}

// actually this not entireley correct - however, for the purpose of our game it's sufficient: one colour must be greater to meet the criterion
data class GameSet(val red: Int, val green: Int, val blue: Int) : Comparable<GameSet> {
    override fun compareTo(other: GameSet): Int = when {
        (this.red > other.red) || (this.green > other.green) || (this.blue > other.blue) -> 1
        (this.red == other.red) && (this.green == other.green) && (this.blue == other.blue) -> 0
        else -> -1
    }

    fun power() = red * green * blue
}

data class Game(val id: Int, val sets: List<GameSet>)

fun Game.minimumSet(): GameSet = GameSet(
    sets.map { it.red }.max(),
    sets.map { it.green }.max(),
    sets.map { it.blue }.max()
)

fun List<String>.toGameSet(): GameSet {
    val red = this.find { it.contains("red") }?.let { it.substringBefore(" red").toInt() } ?: 0
    val green = this.find { it.contains("green") }?.let { it.substringBefore(" green").toInt() } ?: 0
    val blue = this.find { it.contains("blue") }?.let { it.substringBefore(" blue").toInt() } ?: 0
    return GameSet(red, green, blue)
}