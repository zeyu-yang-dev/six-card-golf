package io.github.zeyuyangdev.sixcardgolf.entity

import java.util.Stack

/**
 * Represents the state of a Six Card Golf game.
 *
 * @property players The list of players participating in the game.
 * @property currentPlayerIndex The index of the player who is currently in turn.
 * @property firstPlayerInFinalRound The player who plays first in the last round.
 * @property winningPlayer The player who won the game, or `null` if no winner has been determined yet.
 * @property isLastRound Indicates whether the current round is the last round of the game.
 * @property isFirstRound Indicates whether the current round is the first round of the game.
 * @property discardStack The stack of discarded cards, initialized as an empty ArrayDeque.
 * @property drawStack The stack of cards available for drawing.
 *
 * @constructor Creates a round of game
 */
class SixCardGolf(val players: List<Player>) {

    var currentPlayerIndex: Int = 0

    var firstPlayerInFinalRound: Player? = null

    var winningPlayer: Player? = null

    //-----------------------------------------------------------------
    var isLastRound: Boolean = false

    var isFirstRound: Boolean = true
    //-----------------------------------------------------------------


    var drawStack: Stack<Card> = Stack()

    var discardStack: Stack<Card> = Stack()

}