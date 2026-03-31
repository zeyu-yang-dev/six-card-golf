package io.github.zeyu.sixcardgolf.service

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}

    /**
     * perform refreshes that are necessary after the final round was played
     */
    fun refreshAfterGameEnd() {}

    /**
     * perform refreshes that are necessary after the turn ended
     */
    fun refreshAfterNextTurn() {}

    /**
     * ???
     */
    fun refreshOnLastRound() {}

    //------------------------------------------------------------------------------------------------------------------

    /**
     * perform refreshes that are necessary after a card is drawn from draw stack
     */
    fun refreshAfterDrawCard() {}

    /**
     * perform refreshes that are necessary after a card is drawn from discarded stack
     */
    fun refreshAfterDrawDiscardedCard() {}

    /**
     * perform refreshes that are necessary after a swapping action, which swaps the hand-card and a deck-card
     */
    fun refreshAfterSwap() {}

    /**
     * perform refreshes that are necessary after a hand-card is discarded
     */
    fun refreshAfterDiscard() {}

    /**
     * perform refreshes that are necessary after a card is revealed via player action
     */
    fun refreshAfterReveal() {}

    //------------------------------------------------------------------------------------------------------------------

    /**
     * perform refreshes in the first round after the first card of a player is revealed
     */
    fun refreshAfterFirstReveal() {}

    /**
     * refresh before revealing all cards before game ends
     */
    fun refreshBeforeGameEnd() {}











}