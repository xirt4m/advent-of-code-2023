fun main() {

    data class Node(
            val name: String,
            val routes: Pair<String, String>
    )

    fun List<Node>.countSteps(
            turnInstructions: List<String>,
            start: String,
            endFilter: (String) -> Boolean
    ): Int {
        var next = first { it.name == start }
        var count = 0
        while (true) {
            turnInstructions.forEach {
                count++
                next = when {
                    it == "R" -> {
                        val nodeOnRight = next.routes.second
                        if (endFilter(nodeOnRight)) return count
                        checkNotNull(find { it.name == nodeOnRight })
                    }

                    it == "L" -> {
                        val nodeOnLeft = next.routes.first
                        if (endFilter(nodeOnLeft)) return count
                        checkNotNull(find { it.name == nodeOnLeft })
                    }

                    else -> throw IllegalStateException("$it is not valid instruction!")
                }
            }
        }
    }

    fun List<String>.parseTurnInstructions() = first().map { it.toString() }

    fun List<String>.pareNetwork() = drop(2).map {
        Node(
                name = it.take(3),
                routes = it.substring(7, 10) to it.substring(12, 15)
        )
    }

    fun part1(input: List<String>): Int {
        return input.pareNetwork().countSteps(
                turnInstructions = input.parseTurnInstructions(),
                start = "AAA",
                endFilter = { it == "ZZZ" }
        )
    }

    fun part2(input: List<String>): Long {
        fun gcd(first: Long, second: Long): Long {
            var a = first
            var b = second
            while (b > 0) {
                val temp = b
                b = a % b
                a = temp
            }
            return a
        }

        fun lcm(first: Long, second: Long): Long = first * (second / gcd(first, second))

        fun lcm(numbers: List<Long>): Long = numbers.drop(1).fold(numbers.first()) { acc, number ->
            lcm(acc, number)
        }

        val network = input.pareNetwork()
        val turnInstructions = input.parseTurnInstructions()
        val endFilter: (String) -> Boolean = { it.endsWith("Z") }
        val startPositions = network.map { it.name }.filter { it.endsWith("A") }
        val steps = startPositions.map {
            network.countSteps(
                    turnInstructions = turnInstructions,
                    start = it,
                    endFilter = endFilter
            ).toLong()
        }
        return lcm(steps)
    }

    val input = readInput("Day08")
    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    val testInput3 = readInput("Day08_test3")
    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)
    part1(input).println()

    check(part2(testInput3) == 6L)
    part2(input).println()
}
