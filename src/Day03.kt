fun main() {
    fun part1(input: List<String>): Int {
        fun Char?.isSymbol() = this != null && this != '.' && !isDigit()
        fun Char2D.hasSymbolNeighbour(i: Int, j: Int): Boolean = onNeighbours(i, j) { char, _, _ ->
            char.isSymbol()
        }.any { isSymbol -> isSymbol }

        val engineSchematic: Char2D = input.toChar2d()
        var sum = 0
        engineSchematic.forEachIndexed { i, lineArray ->
            val line = lineArray.joinToString("")
            var lastNumber = ""
            var hasSymbolNeighbour = false
            line.forEachIndexed { j, char ->
                val isDigit = char.isDigit()
                if (isDigit) {
                    hasSymbolNeighbour = hasSymbolNeighbour || engineSchematic.hasSymbolNeighbour(i, j)
                    lastNumber += char
                }
                if (j == line.length.dec() || !isDigit) {
                    if (lastNumber.isNotEmpty() && hasSymbolNeighbour) {
                        sum += lastNumber.toInt()
                    }
                    lastNumber = ""
                    hasSymbolNeighbour = false
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        fun Char?.isAsterisk() = this == '*'
        fun Char2D.findAsterisks() = buildList {
            this@findAsterisks.forEachIndexed { i, lineArray ->
                lineArray.forEachIndexed { j, char ->
                    if (char.isAsterisk()) add(i to j)
                }
            }
        }

        fun String.firstIndexOfNumber(i: Int): Int {
            var first = i
            while (first > 0 && this[first.dec()].isDigit()) {
                first = first.dec()
            }
            return first
        }

        fun String.lastIndexOfNumber(i: Int): Int {
            var last = i
            while (length > last && this[last].isDigit()) {
                last = last.inc()
            }
            return last
        }

        fun Char2D.resolveNumber(i: Int, j: Int): Int? {
            val line = this[i].joinToString("")
            return line.substring(line.firstIndexOfNumber(j), line.lastIndexOfNumber(j)).toIntOrNull()
        }

        fun Char2D.getNumberNeighbours(i: Int, j: Int) = onNeighbours(i, j) { char, ii, jj ->
            when {
                char?.isDigit() == true -> resolveNumber(ii, jj)
                else -> null
            }
        }

        val engineSchematic: Char2D = input.toChar2d()
        return engineSchematic.findAsterisks().sumOf { asteriskPair ->
            engineSchematic
                    .getNumberNeighbours(asteriskPair.first, asteriskPair.second)
                    .filterNotNull()
                    .toSet()
                    .takeIf { it.size == 2 }
                    ?.let { connectedNumbers -> connectedNumbers.first() * connectedNumbers.last() } ?: 0
        }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
