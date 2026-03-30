package io.github.zeyu.sixcardgolf.view.panes

import io.github.zeyu.sixcardgolf.entity.*
import io.github.zeyu.sixcardgolf.service.*
import io.github.zeyu.sixcardgolf.view.*
import io.github.zeyu.sixcardgolf.view.GameScene.StateOfUI

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class PaneMiddleCards(
    private val rootService: RootService,
    private val gameScene: GameScene
) : Pane<ComponentView>(
    500 + PMC_POS_X,
    PMC_POS_Y,
    PMC_WIDTH,
    PMC_HEIGHT,
    visual = PLAYER_PANE_BG_VISUAL
), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val playerActionService = rootService.playerActionService

    private val handCardView: CardView = createCardView(0).apply {
        // hand card is always disabled
        disableCardView(this)
    }

    private val drawStackCardView: CardView = createCardView(1).apply {
        this.showBack()
        onMouseClicked = {
            if (gameScene.state == StateOfUI.TURN_START) {
                playerActionService.drawCardAction()
                gameScene.setUIState(StateOfUI.HAS_DRAWN)
            }
        }
    }

    private val discardStackCardView: CardView = createCardView(2).apply {
        onMouseClicked = {
            if (gameScene.state == StateOfUI.TURN_START) {
                playerActionService.drawDiscardedCardAction()
                gameScene.setUIState(StateOfUI.HAS_DRAWN_DISCARDED)
            }
            if (gameScene.state == StateOfUI.HAS_DRAWN) {
                playerActionService.discard(true)
                gameScene.setUIState(StateOfUI.TURN_START)
            }
        }
    }

    // handCardView is always disabled, no need to update interactivity for handCardView
    private val cardViews: List<CardView> = listOf(drawStackCardView, discardStackCardView)

    private fun createCardView(col: Int): CardView {
        val cardView = CardView(
            posX = (CARD_WIDTH + HORIZ_DIS_BTW_CARDS) * col,
            posY = 0,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = ImageVisual(cardImageLoader.blankImage),
            back = ImageVisual(cardImageLoader.backImage)
        ).apply {
            this.showFront()
        }
        return cardView
    }

    private fun refreshThisPane() {
        val currentGame = rootService.currentGame
        val currentPlayer = currentGame.players[currentGame.currentPlayerIndex]

        // refresh handCardView:
        // handCardView always displays a card front (blank or a normal card)
        val handCardImage = currentPlayer.handCard?.let {
            cardImageLoader.frontImageFor(it.cardSuit, it.cardValue)
        } ?: cardImageLoader.blankImage

        handCardView.frontVisual = ImageVisual(handCardImage)

        // refresh drawStackCardView:
        val drawStack = currentGame.drawStack
        drawStackCardView.apply {
            if (drawStack.isNotEmpty()) this.showBack() else this.showFront()
        }

        // refresh discardStackCardView:
        val discardStack = currentGame.discardStack
        val topCardImage = discardStack.peek()?.let {
            cardImageLoader.frontImageFor(it.cardSuit, it.cardValue)
        } ?: cardImageLoader.blankImage

        discardStackCardView.frontVisual = ImageVisual(topCardImage)

    }

    private fun disableCardView(cardView: ComponentView) {
        cardView.isDisabled = true
        cardView.opacity = 0.5
    }

    private fun disableAllCardViews() {
        cardViews.forEach { disableCardView(it) }
    }

    private fun enableCardView(cardView: ComponentView) {
        cardView.isDisabled = false
        cardView.opacity = 1.0
    }

    private fun enableAllCardViews() {
        cardViews.forEach { enableCardView(it) }
    }

    private fun updateInteractivity() {

        val currentGame = rootService.currentGame

        // Firstly enable all cardViews (except for handCardView, which is always disabled)
        enableAllCardViews()

        // Condition 1: In the first round
        if (currentGame.isFirstRound) disableAllCardViews()

        when (gameScene.state) {
            // Condition 2: A turn just started
            StateOfUI.TURN_START -> {}
            // Condition 3: Just drawn from draw-stack
            StateOfUI.HAS_DRAWN -> disableCardView(drawStackCardView)
            // Condition 4: Just drawn from discard-stack
            StateOfUI.HAS_DRAWN_DISCARDED -> disableAllCardViews()
            // Condition 5: Just discarded the hand card, which was drawn from draw-stack
            StateOfUI.HAS_DISCARDED -> disableAllCardViews()
            // Condition 6: When game ends, disable all components
            StateOfUI.GAME_END -> disableAllCardViews()
        }

    }




    init {

        addAll(handCardView)
        addAll(cardViews)

    }

    override fun refreshAfterStartNewGame() {
        gameScene.setUIState(StateOfUI.TURN_START)

        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterFirstReveal() {
        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterReveal() {
        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterDrawCard() {
        gameScene.setUIState(StateOfUI.HAS_DRAWN)

        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterDrawDiscardedCard() {
        gameScene.setUIState(StateOfUI.HAS_DRAWN_DISCARDED)

        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterSwap() {
        refreshThisPane()
        updateInteractivity()
    }


    override fun refreshAfterDiscard() {
        gameScene.setUIState(StateOfUI.HAS_DISCARDED)

        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshAfterNextTurn() {
        disableAllCardViews()
        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = {
                    gameScene.setUIState(StateOfUI.TURN_START)

                    refreshThisPane()
                    enableAllCardViews()
                    updateInteractivity()
                }
            }
        )
    }

    override fun refreshOnLastRound() {
        refreshThisPane()
        updateInteractivity()
    }

    override fun refreshBeforeGameEnd() {
        gameScene.setUIState(StateOfUI.GAME_END)

        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BEFORE_REVEAL_ALL).apply {
                onFinished = {
                    refreshThisPane()
                    updateInteractivity()
                }
            }
        )


    }

    override fun refreshAfterGameEnd() {
        refreshThisPane()
        updateInteractivity()
    }





}