package io.github.zeyuyangdev.sixcardgolf.entity

/**
 * Represents a playing card with a suit and a value.
 *
 * @property cardSuit The suit of the card, such as hearts, diamonds, clubs, or spades.
 * @property cardValue The value of the card, such as 2, 3, 4, ..., 10, Jack, Queen, King, or Ace.
 * @property isRevealed Indicates whether the card has been revealed or is face-down.
 *
 * @constructor Creates a new `Card` instance with the specified suit and value.
 */
data class Card(val cardSuit: CardSuit, val cardValue: CardValue) {

    var isRevealed: Boolean = false

    /**
     * Returns a string representation of the card.
     */
    override fun toString() = "$cardSuit$cardValue"

}