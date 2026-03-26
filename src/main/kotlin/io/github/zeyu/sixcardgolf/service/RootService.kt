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
     * The currently active game. Can be `null`, if no game has started yet.
     */
    var currentGame : SixCardGolf? = null
    // lateinit var currentGame : SixCardGolf // 可避免null check



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