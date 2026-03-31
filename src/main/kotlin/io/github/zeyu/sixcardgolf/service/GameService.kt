package io.github.zeyu.sixcardgolf.service

import io.github.zeyu.sixcardgolf.entity.*



/**
 * Service layer class that provides the logic for actions not directly related to a single player.
 */
class GameService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Starts a new game, including:
     * 1. Check duplicated player names, if duplicated, throws out an exception.
     * 2. Initialize the property currentGame (an instance of class SixCardGolf) in RootService.
     * 3. Create the draw-stack.
     * 4. Initialize the discard-stack.
     * 5. Distribute the initial deck-cards for every player.
     *
     * @param playerNames The list of player names, length between 2 and 4, order already been set.
     * @param testMode Whether the game is started in test mode.
     */
    fun startNewGame(playerNames: List<String>, testMode: Boolean = false) {

        // 1. Check duplicated player names and number of player names:
        require(playerNames.toSet().size == playerNames.size) {"Duplicated player names found!"}
        require(playerNames.size in 2..4) {"Number of players must be between 2 and 4!"}

        // 2. Initialize the property currentGame (an instance of class SixCardGolf) in RootService:
        val players: List<Player> = playerNames.map { Player(it) }
        rootService.currentGame=SixCardGolf(players)

        // 3. Create the draw-stack:
        rootService.cardService.createDrawStack(testMode)

        // 4. Initialize the discard-stack.
        rootService.cardService.initializeDiscardStack()

        // 5. Distribute the initial deck-cards for every player.
        val currentGame = rootService.currentGame
        for (player in currentGame.players) {
            for (i in 0..2) {
                player.topRow[i] = currentGame.drawStack.pop()
                player.bottomRow[i] = currentGame.drawStack.pop()
            }
        }

        onAllRefreshables { refreshAfterStartNewGame() }
    }


    /**
     * Calls the last round.
     * This methode is called, when:
     * 1. Both rows of a player are removed.
     * 2. All card of a player are revealed.
     */
    private fun callLastRound() {
        val currentGame = rootService.currentGame
        val players = currentGame.players

        val currentPlayerIndex = currentGame.currentPlayerIndex
        val nextPlayerIndex = (currentPlayerIndex + 1) % players.size

        // The next player is the first player in final round.
        if (currentGame.firstPlayerInFinalRound == null) {
            currentGame.firstPlayerInFinalRound = players[nextPlayerIndex]
        }

        currentGame.isLastRound = true

        // Give the control to the next player:
        // currentGame.currentPlayerIndex = nextPlayerIndex

        onAllRefreshables { refreshOnLastRound() }

        // rootService.gameService.nextTurn()
    }



    /**
     * Prepares for the next turn, including:
     * 1. Checks whether a row of the current player can be removed.
     * 2. Checks whether both rows of the current player are removed, if so, mark the winning player and call last round.
     * 3. Checks whether all card of the current player are revealed, if so, call last round.
     * 4. Ends the game if all players have made their turn in the last round.
     * 5. Ends the first round if all players have made their turn in the first round.
     * 6. Gives the command to the next player.
     */
    internal fun nextTurn() {
        val currentGame = rootService.currentGame
        val players = currentGame.players
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val currentPlayer = players[currentPlayerIndex]
        val discardStack = currentGame.discardStack

        // 1. Checks whether a row of the current player can be removed.

        // Check top row for identical card value, the cards must all be revealed.
        if (currentPlayer.topRow.all { it != null && it.isRevealed && it.cardValue == currentPlayer.topRow[0]?.cardValue }) {
            // Set all cards to null and give them to discard-stack if they have identical values
            for (i in 0..2) {
                discardStack.push(currentPlayer.topRow[i])
                currentPlayer.topRow[i] = null
            }
        }

        // Check bottom row for identical card value, the cards must all be revealed.
        if (currentPlayer.bottomRow.all { it != null && it.isRevealed &&
                    it.cardValue == currentPlayer.bottomRow[0]?.cardValue }) {
            // Set all cards to null and give them to discard-stack if they have identical values
            for (i in 0..2) {
                discardStack.push(currentPlayer.bottomRow[i])
                currentPlayer.bottomRow[i] = null
            }
        }

        // 2. Checks whether both rows of the current player are removed, if so, mark the winning player and call last round.
        if (currentPlayer.topRow[0] == null && currentPlayer.bottomRow[0] == null && currentGame.winningPlayer == null) {
            currentGame.winningPlayer = currentPlayer
            callLastRound()
            // no need to quit nextRound when the last round starts:
            // return
        }

        // 3. Checks whether all cards of the current player are revealed, if so, call last round.
        //    This includes: I. all six cards are revealed  II. one row is removed, another row is revealed
        //    When both rows removed, the condition for if is also true, but will be handled in 2.
        if ((currentPlayer.topRow + currentPlayer.bottomRow).all { it == null || it.isRevealed }) {
            callLastRound()
            // no need to quit nextRound when the last round starts:
            // return
        }

        // 4. Ends the game if all players have made their turn in the last round.
        // End the game if all players have made their turn in the last round
        if (currentGame.isLastRound &&
            players[(currentPlayerIndex + 2) % players.size] == currentGame.firstPlayerInFinalRound) {
            onAllRefreshables { refreshBeforeGameEnd() }
            return
        }

        // 5. Ends the first round if all players have made their turn in the first round.
        if (currentGame.isFirstRound && currentPlayerIndex == players.size - 1)
            currentGame.isFirstRound = false

        // 6. Gives the command to the next player.
        currentGame.currentPlayerIndex = (currentGame.currentPlayerIndex + 1) % players.size



        onAllRefreshables { refreshAfterNextTurn() }
    }


    /**
     *
     */
    internal fun endGame() {
        onAllRefreshables { refreshAfterGameEnd() }
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param valueNeeded The value of the card to find.
     *
     * @return The index of the wanted card in the draw stack, -1 if not found.
     */
    private fun findCardInStack(valueNeeded: CardValue): Int {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}

        return currentGame.drawStack.indexOfFirst { it.cardValue ==  valueNeeded }
    }

    /**
     * @param indexOfCard The index of the card, which is wished to be on top of the draw stack.
     */
    private fun swapToTop(indexOfCard: Int) {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val drawStack = currentGame.drawStack

        val cardNeeded = drawStack[indexOfCard]
        val cardOnTop = drawStack.peek()

        drawStack[indexOfCard] = cardOnTop
        drawStack[drawStack.size - 1] = cardNeeded
    }

    /**
     * Search for two cards with identical value in a row for the current player.
     * If not found, return TWO.
     *
     * @return The CardValue of the card, CardValue.TWO if not found.
     */
    private fun twoIdenticalValuesSameRow(): CardValue {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]

        // store the card values, which have been observed in a row
        val visibleValues: MutableSet<CardValue> = mutableSetOf()

        // check top row:
        if (currentPlayer.topRow.all { it != null }) {
            for (card in currentPlayer.topRow.filter { it!!.isRevealed }) {
                if (card!!.cardValue in visibleValues) {
                    return card.cardValue
                } else {
                    visibleValues.add(card.cardValue)
                }
            }
        }

        visibleValues.clear()

        // check bottom row:
        if (currentPlayer.bottomRow.all { it != null }) {
            for (card in currentPlayer.bottomRow.filter { it!!.isRevealed }) {
                if (card!!.cardValue in visibleValues) {
                    return card.cardValue
                } else {
                    visibleValues.add(card.cardValue)
                }
            }
        }

        return CardValue.TWO
    }

    /**
     * If the current player has a row with two revealed cards with identical values,
     * and there is one more card with same value in the draw stack,
     * this methode will move the card to the top of the draw stack.
     *
     * If such situation doesn't stand, then move a TWO to top of the draw stack if available,
     * if TWO is also not available, move a KING to top of the draw stack if available.
     */
    internal fun showMiracle() {

        val cardValueNeeded = twoIdenticalValuesSameRow()

        val indexOfCardNeeded = findCardInStack(cardValueNeeded)

        // If the card was found available in stack:
        if (indexOfCardNeeded != -1) {
            swapToTop(indexOfCardNeeded)
        } else {
            // If not found, move an ACE to top of the draw stack if available:
            val indexOfKing = findCardInStack(CardValue.KING)
            if (indexOfKing != -1) {
                swapToTop(indexOfKing)
            }
        }
    }




}