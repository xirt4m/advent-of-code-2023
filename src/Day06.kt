fun main() {
    fun String.parse(prefix: String, numberMapper: (String) -> List<Long>) = numberMapper(removePrefix(prefix))

    fun calculateWinsProduct(recordTimes: List<Long>, recordDistances: List<Long>) =
        recordTimes.zip(recordDistances).fold(1) { acc, (recordTime, recordDistance) ->
            val wins = (0..<recordTime).count {
                val buttonPress = it
                val bootSpeed = it * 1
                val raceTime = recordTime - buttonPress

                val travelled = raceTime * bootSpeed
                travelled > recordDistance
            }
            acc * wins
        }

    fun part1(input: List<String>): Int {
        fun String.parseNumbers() = split(" ").map { it.trim() }.filterNot { it.isEmpty() }.map { it.toLong() }

        val recordTimes = input.first.parse("Time:") { it.parseNumbers() }
        val recordDistances = input.last.parse("Distance:") { it.parseNumbers() }

        return calculateWinsProduct(recordTimes, recordDistances)
    }

    fun part2(input: List<String>): Int {
        fun String.parseNumbers() = listOf(replace(" ", "").toLong())

        val recordTimes = input.first.parse("Time:") { it.parseNumbers() }
        val recordDistances = input.last.parse("Distance:") { it.parseNumbers() }

        return calculateWinsProduct(recordTimes, recordDistances)
    }

    val input = readInput("Day06")
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    part1(input).println()

    check(part2(testInput) == 71503)
    part2(input).println()
}
