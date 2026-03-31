package io.github.zeyu.sixcardgolf.service

import io.github.zeyu.sixcardgolf.entity.*
import java.util.Stack



/**
 * Provides card-related services, including:
 * 1. creating a draw stack of cards.
 * 2.
 *
 * @property rootService The root service for managing game state.
 */
class CardService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Creates a draw-stack of 52 cards.
     * Then pass this stack to the property "drawStack" to the instance of SixCardGolf (currentGame).
     *
     * @param testMode If `false`, creates a standard 52-card deck;
     *                 If `true`, uses `shortDeck()` values repeated to make 52 cards.
     * @return A stack of cards configured based on the testMode.
     */
    internal fun createDrawStack(testMode: Boolean = false): Stack<Card> {
        val deck = Stack<Card>()

        if (testMode) {
            // Get the short deck with 3 values
            val shortDeckValues = CardValue.shortDeck().toList()

            //-------------------------------------------------------------------------

            // Repeat values until there are 13
            val repeatedValues = mutableListOf<CardValue>()
            // repeat 4 times to get 12 values
            repeat(4) {
                repeatedValues.addAll(shortDeckValues)
            }
            // Add one more value to make it 13
            repeatedValues.add(shortDeckValues[0])

            //-------------------------------------------------------------------------

            // Generate a test draw-stack of 52 cards by combining each suit with the repeated values
            for (suit in CardSuit.entries) {
                for (value in repeatedValues) {
                    deck.push(Card(suit, value))
                }
            }

        } else {
            // Generate a standard draw-stack of 52 cards
            for (suit in CardSuit.entries) {
                for (value in CardValue.entries) {
                    deck.push(Card(suit, value))
                }
            }
        }

        // Shuffle the deck
        deck.shuffle()

        // Pass the created stack to the property "drawStack" to the instance of SixCardGolf (currentGame).
        rootService.currentGame.drawStack = deck

        return deck
    }


    /**
     * Move top card from draw-stack to discard-stack.
     * After moved to discard-stack, the top card is revealed.
     */
    internal fun initializeDiscardStack() {
        // This func will only be called if the game is already initialized
        // Remove the card at the top of draw-stack:
        val topCard = rootService.currentGame.drawStack.pop()
        // Add the card on the top of the discard-stack
        rootService.currentGame.discardStack.push(topCard)
        // Reveal the card:
        topCard.isRevealed = true
    }
}
