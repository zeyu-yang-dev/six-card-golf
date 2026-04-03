package io.github.zeyuyangdev.sixcardgolf.entity

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
    var topRow: MutableList<Card?> = mutableListOf(null, null, null)
    var bottomRow: MutableList<Card?> = mutableListOf(null, null, null)

    //------------------------------------------------------------------------------------------------------------------
    /**
     * The score of all revealed cards of this player.
     * Calculated dynamically based on current card state.
     */
    val deckScore: Int
        get() {
            // Condition 1: if both rows are removed (or not initialized)
            if (topRow[0] == null && bottomRow[0] == null) return 0

            // Condition 2: if only one row left: (the other row is removed)
            // Calculate individual row scores, considering only revealed cards
            // Assuming the row is not removed (cards are not null)
            if (topRow[0] == null || bottomRow[0] == null) {
                val remainingRow = if (topRow[0] != null) topRow else bottomRow
                return remainingRow
                    .filterNotNull()
                    .filter { it.isRevealed }
                    .sumOf { it.cardValue.score }
            }

            // Condition 3: if both rows are not all null (both rows are not removed)
            var totalScore = 0

            for (i in 0..2) {
                // in this condition, both rows are not removed
                val topCard = topRow[i]!!
                val bottomCard = bottomRow[i]!!

                // scan for columns of pairs
                if (topCard.isRevealed &&
                    bottomCard.isRevealed &&
                    topCard.cardValue == bottomCard.cardValue
                    ) {
                    continue
                }

                totalScore += if (topCard.isRevealed) topCard.cardValue.score else 0
                totalScore += if (bottomCard.isRevealed) bottomCard.cardValue.score else 0
            }
            return totalScore
        }
    //------------------------------------------------------------------------------------------------------------------
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

        return """
            Player:           $playerName
            Score:            $deckScore
            Top Row:      ${rowToString(topRow)}
            Bottom Row: ${rowToString(bottomRow)}
        """.trimIndent() // removes the leading empty line
    }

}