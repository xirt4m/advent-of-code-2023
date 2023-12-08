fun main() {

    data class Hand(
            val cards: String,
            val bid: Int
    )

    data class RankedHand(
            val hand: Hand,
            val rank: Int
    )

    fun getCardComparator(cardStrengths: List<String>): Comparator<Char> = Comparator { a, b ->
        cardStrengths.indexOf(a.toString()).compareTo(cardStrengths.indexOf(b.toString()))
    }

    fun getSameTypeComparator(cardStrengths: List<String>): Comparator<String> = Comparator { a, b ->
        val cardComparator = getCardComparator(cardStrengths)
        (0..a.length).forEach {
            cardComparator.compare(a[it], b[it]).takeIf { it != 0 }?.let { return@Comparator it }
        }
        0
    }

    fun getRankedHandComparator(cardStrengths: List<String>): Comparator<RankedHand> = Comparator { a, b ->
        val comparedRank = a.rank.compareTo(b.rank)
        if (comparedRank == 0) {
            getSameTypeComparator(cardStrengths).compare(a.hand.cards, b.hand.cards)
        } else {
            comparedRank
        }
    }

    fun List<String>.parseHands() = map {
        val (card, bid) = it.split(" ")
        Hand(card, bid.toInt())
    }

    fun String.countCards(): Map<Char, Int> = associateWith { card ->
        count { it == card }
    }

    fun List<String>.getWinnings(cardStrength: List<String>, handRankProvider: (String) -> Int): Int {
        val rankedCards = parseHands().map { hand ->
            RankedHand(hand = hand, rank = handRankProvider(hand.cards))
        }.sortedBy { it.rank }

        return rankedCards.sortedWith(getRankedHandComparator(cardStrength)).foldIndexed(0) { i, acc, rankedHand ->
            acc + (i + 1) * rankedHand.hand.bid
        }
    }

    fun part1(input: List<String>): Int {
        val cardStrength = listOf("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2").reversed()

        fun String.getHandRank(): Int {
            val sameCardCount = countCards().values

            // 7
            val fiveOfKind = sameCardCount.contains(5)

            // 6
            val fourOfKind = sameCardCount.contains(4)

            // 5
            val fullHouse = sameCardCount.contains(3) && sameCardCount.contains(2)

            // 4
            val threeOfKind = sameCardCount.contains(3)

            // 3
            val twoPairs = sameCardCount.count { it == 2 } == 2

            // 2
            val onePair = sameCardCount.count { it == 2 } == 1

            return when {
                fiveOfKind -> 7
                fourOfKind -> 6
                fullHouse -> 5
                threeOfKind -> 4
                twoPairs -> 3
                onePair -> 2
                else -> 1
            }
        }

        return input.getWinnings(cardStrength) { it.getHandRank() }
    }

    fun part2(input: List<String>): Int {
        val cardStrength = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J").reversed()

        fun String.getHandRank(): Int {
            val jokers = count { it == 'J' }

            val sameCardCount = countCards().toMutableMap().apply {
                remove('J')
            }.values

            // 7
            val fiveOfKind = sameCardCount.contains(5) ||
                    (sameCardCount.contains(4) && jokers >= 1) ||
                    (sameCardCount.contains(3) && jokers >= 2) ||
                    (sameCardCount.contains(2) && jokers >= 3) ||
                    (sameCardCount.contains(1) && jokers >= 4) ||
                    jokers >= 5

            // 6
            val fourOfKind = sameCardCount.contains(4) ||
                    (sameCardCount.contains(3) && jokers >= 1) ||
                    (sameCardCount.contains(2) && jokers >= 2) ||
                    (sameCardCount.contains(1) && jokers >= 3) ||
                    jokers >= 4

            // 5
            val fullHouse = sameCardCount.contains(3) && sameCardCount.contains(2) ||
                    sameCardCount.contains(3) && jokers >= 1 ||
                    sameCardCount.count { it == 2 } == 2 && jokers >= 1 ||
                    sameCardCount.count { it == 2 } == 1 && jokers >= 2 ||
                    jokers >= 4

            // 4
            val threeOfKind = (sameCardCount.contains(3) ||
                    (sameCardCount.contains(2) && jokers >= 1) ||
                    (sameCardCount.contains(1) && jokers >= 2) ||
                    jokers >= 3) && !fullHouse

            // 3
            val twoPairs = sameCardCount.count { it == 2 } == 2 ||
                    (sameCardCount.count { it == 2 } == 1 && jokers >= 1) ||
                    jokers >= 2

            // 2
            val onePair = sameCardCount.count { it == 2 } == 1 || jokers >= 1

            return when {
                fiveOfKind -> 7
                fourOfKind -> 6
                fullHouse -> 5
                threeOfKind -> 4
                twoPairs -> 3
                onePair -> 2
                else -> 1
            }
        }

        return input.getWinnings(cardStrength) { it.getHandRank() }
    }

    val input = readInput("Day07")
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}
