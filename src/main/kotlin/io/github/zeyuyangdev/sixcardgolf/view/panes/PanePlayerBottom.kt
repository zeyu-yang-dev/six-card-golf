package io.github.zeyuyangdev.sixcardgolf.view.panes

import io.github.zeyuyangdev.sixcardgolf.service.*
import io.github.zeyuyangdev.sixcardgolf.view.*
import io.github.zeyuyangdev.sixcardgolf.view.GameScene.StateOfUI
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.ComponentView

class PanePlayerBottom(
    rootService: RootService,
    gameScene: GameScene
) : PanePlayer(
    rootService,
    gameScene,
    (SCREEN_WIDTH - CARD_WIDTH * 3 - HORIZ_DIS_BTW_CARDS * 2) / 2,
    SCREEN_HEIGHT - PPL_HEIGHT - PPT_DIS_TO_TOP,
    PPL_WIDTH,
    PPL_HEIGHT
) {
    private val playerActionService = rootService.playerActionService

    init {
        addPaneComponents()
        initializePaneComponents()
    }

    override fun getPlayerOffset(numOfPlayers: Int): Int {
        // This pane always shows the deck of the current player.
        return 0
    }

    private fun enableCardView(cardView: ComponentView) {
        cardView.isDisabled = false
        cardView.opacity = 1.0
    }

    private fun enableAllCardViews() {
        cardViews.forEach { enableCardView(it) }
    }

    private fun disableCardView(cardView: ComponentView) {
        cardView.isDisabled = true
        cardView.opacity = 0.5
    }

    private fun disableAllCardViews() {
        cardViews.forEach { disableCardView(it) }
    }

    private fun disableRevealedCardViews() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]
        val cards = currentPlayer.topRow + currentPlayer.bottomRow

        // disable cardViews for all revealed cards
        for (i in cardViews.indices) {
            if (cards[i]?.isRevealed == true) {
                disableCardView(cardViews[i])
            }
        }
    }

    private fun updateInteractivity() {
        val currentGame = rootService.currentGame

        enableAllCardViews()

        // Condition 1: In the first round
        if (currentGame.isFirstRound) disableRevealedCardViews()

        when (gameScene.state) {
            // Condition 2: A turn just started
            StateOfUI.TURN_START -> disableRevealedCardViews()
            // Condition 3: Just drawn from draw-stack
            StateOfUI.HAS_DRAWN -> {}
            // Condition 4: Just drawn from discard-stack
            StateOfUI.HAS_DRAWN_DISCARDED -> {}
            // Condition 5: Just discarded the hand card, which was drawn from draw-stack
            StateOfUI.HAS_DISCARDED -> disableRevealedCardViews()
            // Condition 6: When game ends, disable all components
            StateOfUI.GAME_END -> disableAllCardViews()
        }
    }

    private fun initializePaneComponents() {
        // define the behavior of each cardView upon click
        for (i in 0..5) {
            cardViews[i].apply {
                onMouseClicked = {
                    if (gameScene.state == StateOfUI.TURN_START) {
                        playerActionService.revealAction(i)
                    }
                    if (gameScene.state in setOf(StateOfUI.HAS_DRAWN, StateOfUI.HAS_DRAWN_DISCARDED)) {
                        playerActionService.swap(i)
                    }
                }
            }
        }
    }

    private fun updateThisPane() {
        refreshThisPane()
        updateInteractivity()
    }
    //------------------------------------------------------------------------------------------------------------------
    override fun refreshAfterFirstReveal() = updateThisPane()
    override fun refreshAfterReveal() = updateThisPane()
    override fun refreshAfterSwap() = updateThisPane()
    override fun refreshOnLastRound() = updateThisPane()
    override fun refreshAfterGameEnd() = updateThisPane()

    override fun refreshAfterStartNewGame() {
        gameScene.setUIState(StateOfUI.TURN_START)
        updateThisPane()
    }

    override fun refreshAfterDrawCard() {
        gameScene.setUIState(StateOfUI.HAS_DRAWN)
        updateThisPane()
    }

    override fun refreshAfterDrawDiscardedCard() {
        gameScene.setUIState(StateOfUI.HAS_DRAWN_DISCARDED)
        updateThisPane()
    }

    override fun refreshAfterDiscard() {
        gameScene.setUIState(StateOfUI.HAS_DISCARDED)
        updateThisPane()
    }

    override fun refreshAfterNextTurn() {
        disableAllCardViews()
        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = {
                    gameScene.setUIState(StateOfUI.TURN_START)
                    updateThisPane()
                }
            }
        )
    }

    override fun refreshBeforeGameEnd() {
        gameScene.setUIState(StateOfUI.GAME_END)
        updateThisPane()

        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BEFORE_REVEAL_ALL).apply {
                onFinished = {
                    rootService.gameService.revealAllCards()
                    rootService.gameService.removeIdenticalRows()
                    updateThisPane()
                }
            }
        )
    }
}