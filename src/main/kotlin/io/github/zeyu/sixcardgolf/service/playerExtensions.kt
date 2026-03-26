package io.github.zeyu.sixcardgolf.service

import io.github.zeyu.sixcardgolf.entity.CardValue
import io.github.zeyu.sixcardgolf.entity.Player



/**
 * The score of all revealed cards
 * Everytime deckScore is visited, score will be automatically calculated.
 */
val Player.computedDeckScore: Int
    get() {

        val scoreMap = mapOf(
            CardValue.ACE to 1,
            CardValue.TWO to -2,
            CardValue.THREE to 3,
            CardValue.FOUR to 4,
            CardValue.FIVE to 5,
            CardValue.SIX to 6,
            CardValue.SEVEN to 7,
            CardValue.EIGHT to 8,
            CardValue.NINE to 9,
            CardValue.TEN to 10,
            CardValue.JACK to 10,
            CardValue.QUEEN to 10,
            CardValue.KING to 0
        )

        //-----------------------------------------------------------------

        // Condition 1: if both rows are removed (or not initialized)
        if (topRow[0] == null && bottomRow[0] == null) {
            return 0
        }

        //-----------------------------------------------------------------

        // Condition 2: if only one row left: (the other row is removed)

        // Calculate individual row scores, considering only revealed cards
        // Assuming the row is not removed (cards are not null)

        if (topRow[0] == null || bottomRow[0] == null) {
            val remainingRow = topRow.takeIf { it[0] != null } ?: bottomRow
            return remainingRow.filter { it!!.isRevealed }.sumOf { scoreMap[it!!.cardValue] ?: 0 }
        }

        //-----------------------------------------------------------------

        // Condition 3: if both rows are not all null (both rows are not removed)

        var totalScore = 0

        for (i in 0..2) {
            val topCard = topRow[i]
            val bottomCard = bottomRow[i]
            if (topCard!!.isRevealed && bottomCard!!.isRevealed && topCard.cardValue == bottomCard.cardValue) {
                continue
            }
            // ?: checks whether scoreMap[topCard.value] is null, though unlikely
            totalScore += if (topCard.isRevealed) (scoreMap[topCard.cardValue] ?: 0) else 0
            totalScore += if (bottomCard!!.isRevealed) (scoreMap[bottomCard.cardValue] ?: 0) else 0
        }
        return totalScore

    }
