fun main() {
    fun Maze.reachablePositions(currentPositions: List<Int>, exceptFor: Int) =
        currentPositions
            .flatMap { pos ->
                pos.reachableNeighbours()
                    .filter { data[it] != '.' && distances[it] == 0 && it != exceptFor }
            }

    fun part1(input: List<String>): Int {
        with(Maze.from(input)) {
            //data.displayAsBoard(maze.width)
            //distances.displayAsBoard(maze.width)

            var distance = 0
            val ignore = data.indexOf('S') //.also { println("start $it") }
            var currentPositions = listOf(ignore)
            var reachablePositions = reachablePositions(currentPositions, ignore) //.also { println("reachable: $it")}
            while (reachablePositions.isNotEmpty()) {
                distance++
                for (r in reachablePositions) {
                    distances[r] = distance
                }
                currentPositions = reachablePositions
                reachablePositions = reachablePositions(currentPositions, ignore) //.also { println("reachable: $it")}
                //maze.distances.displayAsBoard(maze.width)
            }

            return distances.max()
        }
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day10/test-1")) == 8)
    //check(part2(readInput("dayxx/test-1")) == 1)

    val input = readInput("day10/input")
    part1(input).println()
    // part2(input).println()
}


class Maze(val data: CharArray, val width: Int, val height: Int) : LinearGridBoard(width, height) {

    val distances = IntArray(width * height) { 0 }

    fun Field.reachableNeighbours(): List<Field> {
        return Direction.entries.mapNotNull {
            val neighbour = this.neighbour(it)
            if (neighbour != null && (data[neighbour] in Reachable.forDirection(it)))
                neighbour
            else
                null
        }
    }

    companion object {
        fun from(input: List<String>): Maze {
            require(input.map { it.length }.distinct().size == 1) { "expect all lines are of same length" }
            val width = input[0].length
            val height = input.size
            return Maze(input.joinToString("").toCharArray(), width, height)
        }
    }
}

enum class Reachable(val symbols: List<Char>) {
    NORTH(listOf('|', '7', 'F')),
    SOUTH(listOf('|', 'J', 'L')),
    EAST(listOf('-', '7', 'J')),
    WEST(listOf('-', 'F', 'L'));

    companion object {
        fun forDirection(direction: Direction) = when (direction) {
            Direction.EAST -> EAST.symbols
            Direction.WEST -> WEST.symbols
            Direction.NORTH -> NORTH.symbols
            Direction.SOUTH -> SOUTH.symbols
        }
    }
}