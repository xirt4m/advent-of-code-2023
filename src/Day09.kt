fun main() {
    fun String.parseNumbers() = split(" ").map { it.toInt() }

    fun List<Int>.calculateDifference() = zipWithNext().map { (a, b) -> b - a }

    fun calculateDifferences(numbers: List<Int>) = buildList<List<Int>> {
        add(numbers)
        do {
            add(last().calculateDifference())
        } while (last().any { it != 0 })
    }

    fun part1(input: List<String>): Int = input.sumOf {
        val differences = calculateDifferences(it.parseNumbers())
        differences.reversed().sumOf { it.last() }
    }

    fun part2(input: List<String>): Int = input.sumOf {
        val differences = calculateDifferences(it.parseNumbers())
        differences.reversed().fold(0) { acc, line ->
            line.first() - acc
        }.toInt()
    }

    val input = readInput("Day09")
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    part1(input).println()
    //check(part1(input) == 1938800261)

    check(part2(testInput) == 2)
    part2(input).println()
    //check(part2(input) == 1112)
}
