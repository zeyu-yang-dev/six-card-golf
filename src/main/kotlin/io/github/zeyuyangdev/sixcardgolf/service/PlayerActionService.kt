package io.github.zeyuyangdev.sixcardgolf.service

import io.github.zeyuyangdev.sixcardgolf.entity.*



/**
 * Notice that [SixCardGolf.isFirstRound] only affects the behavior of [revealAction].
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Draws a card from draw-stack to hand for the current player.
     */
    fun drawCardAction() {
        //-------------------------------------------------------------------------
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        val drawStack = currentGame.drawStack
        //-------------------------------------------------------------------------

        // Check if the current player already has a card in hand.
        if (currentPlayer.handCard != null) {
            throw IllegalStateException("Hand of the player is not empty.")
        }

        // for this action, draw-stack can't be empty
        if (drawStack.size == 0) {
            throw IllegalStateException("For this action, drawStack cannot be empty.")
        }

        // Draw the card to hand and reveal it.
        currentPlayer.handCard = drawStack.pop()
        currentPlayer.handCard!!.isRevealed = true

        onAllRefreshables { refreshAfterDrawCard() }
    }



    /**
     * Draws a card from discard-stack to hand for the current player.
     */
    fun drawDiscardedCardAction() {
        //-------------------------------------------------------------------------
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        val discardStack = currentGame.discardStack
        //-------------------------------------------------------------------------

        // Check if the current player already has a card in hand.
        if (currentPlayer.handCard != null) {
            throw IllegalStateException("Hand of the player is not empty.")
        }

        // In fact, discard-stack will never be empty
        if (discardStack.size == 0) {
            throw IllegalStateException("For this action, discardStack cannot be empty.")
        }

        // Draw the card to hand and reveal it.
        currentPlayer.handCard = discardStack.pop()
        currentPlayer.handCard!!.isRevealed = true

        onAllRefreshables { refreshAfterDrawDiscardedCard() }
    }



    /**
     * Reveals a card in the 6-card-deck for the current player
     *
     * @param deckIndex The index of the card to be revealed, ranges from 0 to 5.
     */
    fun revealAction(deckIndex: Int) {
        //-------------------------------------------------------------------------
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        //-------------------------------------------------------------------------

        // Get the card object to reveal:
        val row: MutableList<Card?>
        val indexInRow: Int

        if (deckIndex in 0..2) {
            row = currentPlayer.topRow
            indexInRow = deckIndex
        } else {
            row = currentPlayer.bottomRow
            indexInRow = deckIndex - 3
        }

        val card = row[indexInRow]

        //-------------------------------------------------------------------------

        // Check whether the card is null or already revealed:
        if (card == null || card.isRevealed) {
            throw IllegalArgumentException("The chosen card is null or already revealed.")
        }

        // Reveal the chosen card:
        card.isRevealed = true

        onAllRefreshables { refreshAfterReveal() }

        // If the game is in the first round and the current player hasn't revealed 2 cards yet,
        // the turn doesn't end and [GameService.nextTurn] will not be called.
        if (currentGame.isFirstRound) {
            val numRevealedCards = (currentPlayer.topRow + currentPlayer.bottomRow).count { it?.isRevealed == true }
            if (numRevealedCards < 2) {
                onAllRefreshables { refreshAfterFirstReveal() }
                return
            }
        }

        rootService.gameService.nextTurn()
    }



    /**
     * Discards the hand-card to the discard-stack.
     * This methode have 2 conditions:
     * 1. This methode is called directly after drawCardAction, in this case, the turn doesn't end.
     * - In this case, this methode is called by the player manually.
     * 2. This methode is called after swap, in this case, the turn ends.
     * - In this case, this methode is called automatically after swap action.
     *
     * @param rejectedDrawCard Is true when a swap didn't take place.
     */
    fun discard(rejectedDrawCard: Boolean) {
        //-------------------------------------------------------------------------
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        val discardStack = currentGame.discardStack
        //-------------------------------------------------------------------------

        if (currentPlayer.handCard == null) {
            throw IllegalStateException("Can't discard when handCard is null.")
        }

        discardStack.push(currentPlayer.handCard)
        currentPlayer.handCard = null

        onAllRefreshables { refreshAfterDiscard() }

        // If swap didn't happen, the turn doesn't end.
        if (!rejectedDrawCard) {
            rootService.gameService.nextTurn()
        }

    }



    /**
     * Swaps the hand-card and one of the 6 deck-cards for the current player.
     *
     * @param deckIndex The index of the card to be swapped, ranges from 0 to 5.
     */
    fun swap(deckIndex: Int) {
        //-------------------------------------------------------------------------
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        //-------------------------------------------------------------------------

        // Get the card object from deck-cards to swap:
        val row: MutableList<Card?>
        val indexInRow: Int

        if (deckIndex in 0..2) {
            row = currentPlayer.topRow
            indexInRow = deckIndex
        } else {
            row = currentPlayer.bottomRow
            indexInRow = deckIndex - 3
        }

        //-------------------------------------------------------------------------

        // Check whether one of the both cards is null:
        if (row[indexInRow] == null || currentPlayer.handCard == null) {
            throw IllegalArgumentException("In a swap action, both cards can't be null.")
        }

        // Reveal the deck-card before swap:
        row[indexInRow]!!.isRevealed = true

        // Make the swap:
        val handCard = currentPlayer.handCard
        currentPlayer.handCard = row[indexInRow]
        row[indexInRow] = handCard

        onAllRefreshables { refreshAfterSwap() }

        // Call discard() to discard the hand-card:
        discard(rejectedDrawCard = false)


    }
}