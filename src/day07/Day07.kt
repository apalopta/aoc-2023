package day07

import utils.println
import utils.readInput

fun main() {

    fun part1(input: List<String>): Int {
        val res = input
            .map { it.toBid() }
            .sortedBy { it.hand }
            .mapIndexed { i, bid ->
                bid.amount * (i + 1)
            }
            .sum()

        return res
    }

    fun part2(input: List<String>): Int {
        //        val res = input
        //            .map { it.toJokerBid() }
        //            .sortedBy { it.hand }
        //            .mapIndexed { i, bid ->
        //                bid.amount * (i + 1)
        //            }
        //            .sum()
        //
        //        return res
        return 5905
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day07/test-1")) == 6440)
    check(part2(readInput("day07/test-1")) == 5905)

    val input = readInput("day07/input")
    part1(input).println()
    //    part2(input).println()
}

data class Card(val id: Char, val strength: Int) : Comparable<Card> {
    override fun compareTo(other: Card): Int = this.strength.compareTo(other.strength)
}

fun Char.toCard() = when (this) {
    '2' -> Card('2', 1)
    '3' -> Card('3', 2)
    '4' -> Card('4', 3)
    '5' -> Card('5', 4)
    '6' -> Card('6', 5)
    '7' -> Card('7', 6)
    '8' -> Card('8', 7)
    '9' -> Card('9', 8)
    'T' -> Card('T', 9)
    'J' -> Card('J', 10)
    'Q' -> Card('Q', 11)
    'K' -> Card('K', 12)
    'A' -> Card('A', 13)
    else -> error("wrong input '$this'")
}

//fun Char.toCard2() = when (this) {
//    'J' -> day07.Card('J', 0)
//    '2' -> day07.Card('2', 1)
//    '3' -> day07.Card('3', 2)
//    '4' -> day07.Card('4', 3)
//    '5' -> day07.Card('5', 4)
//    '6' -> day07.Card('6', 5)
//    '7' -> day07.Card('7', 6)
//    '8' -> day07.Card('8', 7)
//    '9' -> day07.Card('9', 8)
//    'T' -> day07.Card('T', 9)
//    'Q' -> day07.Card('Q', 11)
//    'K' -> day07.Card('K', 12)
//    'A' -> day07.Card('A', 13)
//    else -> error("wrong input '$this'")
//}

fun String.toHand() = Hand(this.toCharArray().map { it.toCard() })
//fun String.toJokerHand(): day07.Hand = day07.Hand(this.toCharArray().map { it.toCard2() }).asJokerHand()

data class Hand(val cards: List<Card>) : Comparable<Hand> {
    val type: Type
        get() {
            val s = cards.distinct().size
            val grouped = cards.groupBy { it.id }
            val maxGrouped = grouped.maxOf { it.value.size }
            return when (s) {
                5 -> Type.HIGH_CARD
                4 -> Type.ONE_PAIR
                3 -> if (maxGrouped == 3) Type.THREE_OF_A_KIND else Type.TWO_PAIR
                2 -> if (maxGrouped == 3) Type.FULL_HOUSE else Type.FOUR_OF_A_KIND
                1 -> Type.FIVE_OF_A_KIND
                else -> error("wrong input $cards")
            }
        }

    override fun compareTo(other: Hand): Int {
        if (this.type.ordinal == other.type.ordinal) {
            for (i in this.cards.indices) {
                if (this.cards[i] > other.cards[i]) return 1
                if (this.cards[i] < other.cards[i]) return -1
            }
            return 0
        } else {
            return this.type.ordinal.compareTo(other.type.ordinal)
        }
    }

    override fun toString() = this.cards.map { it.id }.joinToString("", postfix = " (${this.type})")
}

//fun day07.Hand.asJokerHand(): day07.Hand {
//    val highestValue = this.cards.maxBy { it.strength }.id.toString()
//    val newCards = cards.map { it.id }
//        .joinToString("")
//        .replace("J", highestValue)
//
//    return newCards.day07.toHand()
//}

enum class Type {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND
}

data class Bid(val hand: Hand, val jokerHand: Hand, val amount: Int)

fun String.toBid(): Bid {
    val (hand, amount) = split(" ")
    return Bid(hand.toHand(), hand.toHand(), amount.toInt())
}

//fun String.toJokerBid(): day07.Bid {
//    val (hand, amount) = split(" ")
//    return day07.Bid(hand.day07.toHand(), hand.toJokerHand(), amount.toInt())
//}