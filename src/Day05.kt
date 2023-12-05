fun main() {

    data class Entry(
            val destinationStart: Long,
            val sourceStart: Long,
            val range: Long
    )

    val conversions = listOf(
            "seed", "soil", "fertilizer", "water", "light", "temperature", "humidity", "location"
    )

    fun List<String>.parseMaps() = buildMap {
        var key = ""
        val entries = mutableListOf<Entry>()
        drop(2).forEach {
            if (it.contains(":")) {
                if (key.isNotEmpty()) {
                    put(key, entries.toList())
                }
                key = it.removeSuffix(" map:")
                entries.clear()
            } else if (it.isNotEmpty()) {
                val (destination, source, range) = it.split(" ").map { it.toLong() }
                entries.add(Entry(destination, source, range))
            }
        }
        put(key, entries)
    }

    fun List<String>.parseSeeds() = first.removePrefix("seeds: ").split(" ").map { it.toLong() }

    fun Map<String, List<Entry>>.convert(from: String, to: String, input: Long): Long {
        checkNotNull(get("$from-to-$to")).forEach {
            if (input in it.sourceStart..<it.sourceStart + it.range) {
                return it.destinationStart + (input - it.sourceStart)
            }
        }
        return input
    }

    fun Map<String, List<Entry>>.doConversion(seed: Long) =
            conversions.zipWithNext().fold(seed) { acc, (from, to) -> convert(from, to, acc) }

    fun Map<String, List<Entry>>.convertInverse(from: String, to: String, input: Long): Long {
        checkNotNull(get("$from-to-$to")).forEach {
            if (input in it.destinationStart..<it.destinationStart + it.range) {
                return it.sourceStart + (input - it.destinationStart)
            }
        }
        return input
    }

    fun Map<String, List<Entry>>.doInverseConversion(location: Long) =
            conversions.zipWithNext().reversed().fold(location) { acc, (from, to) -> convertInverse(from, to, acc) }

    fun part1(input: List<String>): Long {
        val seeds = input.parseSeeds()
        val map = input.parseMaps()
        return seeds.minOf { map.doConversion(it) }
    }

    fun part2(input: List<String>): Long {
        val seeds = input.parseSeeds().chunked(2)
        var i = 0L
        val map = input.parseMaps()
        while (true) {
            val seed = map.doInverseConversion(++i)
            seeds.forEach {
                if (seed in it.first..<it.first + it.last) {
                    return i
                }
            }
        }
    }

    val input = readInput("Day05")
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    part1(input).println()

    check(part2(testInput) == 46L)
    part2(input).println()
}
