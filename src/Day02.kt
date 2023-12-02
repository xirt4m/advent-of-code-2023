import Color.*

enum class Color {
    RED,
    GREEN,
    BLUE
}

fun main() {
    val availableCubes = mapOf(
            RED to 12,
            GREEN to 13,
            BLUE to 14
    )

    fun String.parseSets() = substring(indexOf(":") + ":".length).split(";")

    fun String.parseCubes() = split(",").associate { cube ->
        val cubeAndCount = cube.trim().split(" ")
        Color.valueOf(cubeAndCount.last.uppercase()) to cubeAndCount.first.toInt()
    }

    fun part1(input: List<String>): Int {
        fun String.parseGameId() = substring("Game ".length, indexOf(":")).toInt()
        fun Map<Color, Int>.moreThanAvailable(): Boolean = map { it.value > (availableCubes[it.key] ?: 0) }.any { it }

        return input.sumOf { line ->
            val impossibleGame = line.parseSets().any { setLine ->
                setLine.parseCubes().moreThanAvailable()
            }
            line.parseGameId().takeIf { impossibleGame.not() } ?: 0
        }
    }

    fun part2(input: List<String>): Int {
        fun List<Map<Color, Int>>.max(color: Color) = map { it[color] ?: 0 }.maxOf { it }

        return input.sumOf { line ->
            val cubes = line.parseSets().map { setLine ->
                setLine.parseCubes()
            }
            cubes.max(RED) * cubes.max(GREEN) * cubes.max(BLUE)
        }
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
