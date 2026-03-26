package io.github.zeyu.sixcardgolf.entity

import io.github.zeyu.sixcardgolf.service.computedDeckScore


/**
 * Represents a player with two rows of cards.
 *
 * @property playerName The name of the player.
 * @property handCard The card currently held by the player, or `null` if none.
 * @property topRow A list of cards in the top row, which contains 3 `null` cards if the row is removed (or not initialized).
 * @property bottomRow A list of cards in the bottom row, which contains 3 `null` cards if the row is removed.
 * @property deckScore The score of all revealed cards, calculated dynamically.
 *
 * @constructor Creates a new Player instance
 */
class Player(val playerName: String) {

    var handCard: Card? = null

    //-------------------------------------------------------------------------

    var topRow: MutableList<Card?> = mutableListOf(null, null, null)

    var bottomRow: MutableList<Card?> = mutableListOf(null, null, null)

    //-------------------------------------------------------------------------

    val deckScore: Int
        get() = computedDeckScore

    //-------------------------------------------------------------------------



    /**
     * Returns a string representation of the player,
     * including their name, score, and the state of both rows.
     *
     * @return A string representing the detailed state of the player and their card rows, formatted for display.
     *
     */
    override fun toString(): String {

        fun rowToString(row: MutableList<Card?>): String {
            return when {
                // to check whether a row is removed (or not initialized)
                // if one of the three cards is null, the other two must be null
                row[0] == null -> "Row is removed."

                else -> row.joinToString(separator = ", ") {
                    if (it!!.isRevealed) it.toString() else "not revealed"
                }
            }
        }

        // val separator = "-".repeat(30)

        // return """
        //     $separator
        //     Player:     $playerName
        //     Score:      $deckScore
        //     Top Row:    ${rowToString(topRow)}
        //     Bottom Row: ${rowToString(bottomRow)}
        //     Hand Card:  $handCard
        //     $separator
        // """.trimIndent() // Trims leading indentation from the multi-line string for proper formatting.

        // This is for the result scene, hence the handcard is deleted
        return """
            Player:           $playerName
            Score:            $deckScore
            Top Row:      ${rowToString(topRow)}
            Bottom Row: ${rowToString(bottomRow)}
        """.trimIndent() // Trims leading indentation from the multi-line string for proper formatting.
    }

}