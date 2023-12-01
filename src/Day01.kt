fun main() {

    fun String.firstDigit() = first { it.isDigit() }
    fun String.lastDigit() = last { it.isDigit() }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val first = line.firstDigit().digitToInt()
            val last = line.lastDigit().digitToInt()
            first * 10 + last
        }
    }

    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            fun findNumber(
                    locateDigit: String.() -> Char,
                    locateSpelledDigit: String.(Char) -> Pair<Int, String>?,
            ): Int {
                val digit = locateDigit(line)
                val spelledDigit = locateSpelledDigit(line, digit)
                return if (spelledDigit == null) {
                    digit.digitToInt()
                } else {
                    digits.indexOf(spelledDigit.second) + 1
                }
            }

            val firstNumber = findNumber(
                    locateDigit = { firstDigit() },
                    locateSpelledDigit = { digit -> substring(0, indexOf(digit)).findAnyOf(digits) }
            )

            val lastNumber = findNumber(
                    locateDigit = { lastDigit() },
                    locateSpelledDigit = { digit -> substring(lastIndexOf(digit)).findLastAnyOf(digits) }
            )

            firstNumber * 10 + lastNumber
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
