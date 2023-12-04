import kotlin.math.pow

fun main() {

    data class Card(
            val id: Int,
            val winningNumbers: Set<Int>,
            val numbers: Set<Int>
    ) {
        val winners = numbers.intersect(winningNumbers).size
        val points = 2.toFloat().pow(winners - 1).toInt()
    }

    fun String.parseId() = removePrefix("Card").trim().takeWhile { it.isDigit() }.toInt()
    fun String.parseNumbers() = split(" ").filterNot { it.isEmpty() }.map { it.toInt() }.toSet()

    fun List<String>.parseCards(): List<Card> = map { line ->
        val cardNumbers = line.substring(line.indexOf(":") + ":".length).split("|")
        Card(
                id = line.parseId(),
                winningNumbers = cardNumbers.first().parseNumbers(),
                numbers = cardNumbers.last().parseNumbers()
        )
    }

    fun part1(input: List<String>): Int = input.parseCards().sumOf { it.points }

    fun part2(input: List<String>): Int {
        fun Map<Int, Card>.countWinners(i: Int): Int {
            val winners = get(i)?.winners ?: 0
            return (0..<winners).sumOf { countWinners(i + it + 1) + 1 }
        }
        return input.parseCards().associateBy { it.id }.let { cardMap ->
            cardMap.values.sumOf { cardMap.countWinners(it.id) + 1 }
        }
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
