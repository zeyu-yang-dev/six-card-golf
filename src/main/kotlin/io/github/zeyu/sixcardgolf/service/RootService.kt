package io.github.zeyu.sixcardgolf.service

import io.github.zeyu.sixcardgolf.entity.SixCardGolf // this class represents the game status
import io.github.zeyu.sixcardgolf.view.Refreshable

/**
 * Main class of the service layer for theSixCardGolf game.
 * Provides access to all other service classes and holds the [currentGame] state for these services to access.
 */
class RootService {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)
    val cardService = CardService(this)

    /**
     * The currently active game.
     */
    lateinit var currentGame : SixCardGolf



    //-------------------------------------------------------------------------



    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    private fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
        cardService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}