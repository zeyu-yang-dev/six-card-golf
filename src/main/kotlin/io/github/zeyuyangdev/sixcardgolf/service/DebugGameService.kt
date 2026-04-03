package io.github.zeyuyangdev.sixcardgolf.service

import io.github.zeyuyangdev.sixcardgolf.entity.*

/**
 * Provides debug
 */
class DebugGameService(private val rootService: RootService) {

    /**
     * If the current player has a row with two revealed cards with identical values,
     * and there is one more card with same value in the draw stack,
     * this method will move the card to the top of the draw stack.
     */
    fun triggerRowRemoval() {

        val cardValueNeeded = findDuplicate() ?: return
        val indexOfCardNeeded = findCardInStack(cardValueNeeded)

        // If the card was found available in draw stack:
        if (indexOfCardNeeded != -1) swapToTop(indexOfCardNeeded)
    }

    /**
     * Checks whether the current player has a row,
     * which contains two cards with the same value.
     * @return The card value, null if not found.
     */
    private fun findDuplicate(): CardValue? {
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]

        return findDuplicate(currentPlayer.topRow) ?: findDuplicate(currentPlayer.bottomRow)
    }

    /**
     * Checks whether a row has two cards with the same value.
     * @return The card value, null if not found.
     */
    private fun findDuplicate(row: MutableList<Card?>): CardValue? {
        if (row.any { it == null }) return null

        val valuesSeen = mutableSetOf<CardValue>()

        for (card in row.filterNotNull().filter { it.isRevealed }) {
            if (card.cardValue in valuesSeen) {
                return card.cardValue
            } else {
                valuesSeen.add(card.cardValue)
            }
        }
        return null
    }

    /**
     * @param valueNeeded The value of the card to find.
     * @return The index of the wanted card in the draw stack, -1 if not found.
     */
    private fun findCardInStack(valueNeeded: CardValue): Int {
        val currentGame = rootService.currentGame
        return currentGame.drawStack.indexOfFirst { it.cardValue ==  valueNeeded }
    }

    /**
     * @param indexOfCard The index of the card, which is wished to be on top of the draw stack.
     */
    private fun swapToTop(indexOfCard: Int) {
        val currentGame = rootService.currentGame
        val drawStack = currentGame.drawStack

        val cardNeeded = drawStack[indexOfCard]
        val cardOnTop = drawStack.peek()

        drawStack[indexOfCard] = cardOnTop
        drawStack[drawStack.size - 1] = cardNeeded
    }
}