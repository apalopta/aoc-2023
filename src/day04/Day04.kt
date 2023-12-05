package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.toCard().value()
        }
    }

    // very slow, but working
    fun part2(input: List<String>): Int {
        val listOfCards = input.map { it.toCard() }

        println("====")
        for ((i, card) in listOfCards.withIndex()) {
//            println(card)
            val wonCards = card.wonCards()
            val count = card.count
//            println("$i won cards: $wonCards")
            repeat(count) {c ->
                repeat(wonCards) {w ->
                    val nextCardIndex = i + w + 1
                    println("nextCard: $nextCardIndex")
                    if (nextCardIndex < listOfCards.size) {
                        listOfCards[nextCardIndex].count++
                    }
                }
            }
        }
        listOfCards.forEach(::println)

        return listOfCards.sumOf { it.count }//.also { println(it) }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day04/test-1")) == 13)
    check(part2(readInput("day04/test-1")) == 30)

    val input = readInput("day04/input")
    part1(input).println()
    part2(input).println()
}

data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>, var count: Int = 1)

fun String.toCard(): Card {
//    println(this)
    val id = substringBefore(":").substringAfter("Card").trim().toInt()
    val winningNumbers = substringBefore("|").substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }
    val numbers = substringAfter("|").split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }

    return Card(id, winningNumbers, numbers).also { println(it) }
}

fun Card.value(): Int {
    val numberOfMatches = winningNumbers.intersect(numbers.toSet()).size
//    println("matches= $numberOfMatches")
    return if (numberOfMatches > 0) {
        2.0.pow(numberOfMatches - 1).toInt()
    } else {
        0
    }//.also { println(it) }
}

fun Card.wonCards(): Int = winningNumbers.intersect(numbers.toSet()).size
