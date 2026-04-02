package io.github.zeyuyangdev.sixcardgolf.view.panes

import io.github.zeyuyangdev.sixcardgolf.entity.*
import io.github.zeyuyangdev.sixcardgolf.service.*
import io.github.zeyuyangdev.sixcardgolf.view.*

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * Abstract base class for all player panes.
 */
abstract class PanePlayer(
    protected val rootService: RootService,
    protected val gameScene: GameScene,
    posX: Number,
    posY: Number,
    width: Number,
    height: Number
) : Pane<ComponentView>(
    posX,
    posY,
    width,
    height,
    visual = PLAYER_PANE_BG_VISUAL
), Refreshable {

    //------------------------------------------------------------------------------------------------------------------
    // FINAL PROPERTIES:
    //------------------------------------------------------------------------------------------------------------------
    protected val cardImageLoader = CardImageLoader()

    protected val cardViews: MutableList<CardView> = mutableListOf()
    protected val labels: MutableList<Label> = mutableListOf()

    //------------------------------------------------------------------------------------------------------------------
    // OPEN PROPERTIES AND METHODS:
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Relative positioning parameters for cardViews and labels
     * overridden in PanePlayerLeftOld
     */
    protected open val cardViewStartX: Double = 0.0
    protected open val labelStartX: Double = CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2 + DIS_BTW_LABEL_AND_CARD

    /**
     * Defines which player this pane should display.
     * @return (offset from current player) toPlayerOfThisPane
     */
    protected abstract fun getPlayerOffset(numOfPlayers: Int): Int

    //------------------------------------------------------------------------------------------------------------------
    // FINAL METHODS:
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns the player displayed by this pane.
     */
    protected fun getPlayerOfThisPane(): Player {
        val currentGame = rootService.currentGame
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val numOfPlayers = currentGame.players.size

        // index offset from current player to the player displayed in this pane
        val offset = getPlayerOffset(numOfPlayers)

        return currentGame.players[(currentPlayerIndex + offset) % numOfPlayers]
    }

    protected fun createCardView(row: Int, col: Int): CardView {
        return CardView(
            posX = cardViewStartX + (CARD_WIDTH + HORIZ_DIS_BTW_CARDS) * col,
            posY = (CARD_HEIGHT + VERT_DIS_BTW_CARDS) * row,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
            back = ImageVisual(cardImageLoader.backImage)
        ).apply { showFront() }
    }

    protected fun createLabel(row: Int): Label {
        return Label(
            posX = labelStartX,
            posY = PLAYER_LABEL_POS_Y + PLAYER_LABEL_HEIGHT * row,
            width = PLAYER_LABEL_WIDTH,
            height = PLAYER_LABEL_HEIGHT,
            text = listOf("Player Name:", "", "Visible Score:", "").getOrElse(row) { "" },
            font = Font(size = 25, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD),
            alignment = Alignment.CENTER_LEFT
        )
    }

    protected fun refreshCardViews() {
        val player = getPlayerOfThisPane()
        val cards = player.topRow + player.bottomRow

        for (i in 0..5) {
            cardViews[i].apply {
                val card = cards[i]
                card?.let {
                    frontVisual = ImageVisual(cardImageLoader.frontImageFor(card.cardSuit, card.cardValue))
                    if (card.isRevealed) showFront() else showBack()
                    isVisible = true
                } ?: run { isVisible = false }
            }
        }
    }

    protected fun refreshLabels() {
        val player = getPlayerOfThisPane()
        val players = rootService.currentGame.players

        // Select the right color for the player displayed in this pane.
        val font = Font(
            size = 25,
            color = PLAYER_COLORS[players.indexOf(player)],
            fontWeight = Font.FontWeight.SEMI_BOLD
        )

        labels[1].text = player.playerName
        labels[1].font = font
        labels[3].text = "${player.deckScore}"
        labels[3].font = font
    }

    /**
     * Refreshes the displays of the cardViews and labels.
     */
    protected fun refreshThisPane() {
        refreshCardViews()
        refreshLabels()
    }

    /**
     * Creates cardViews and labels and add them to this pane.
     */
    protected fun addPaneComponents() {
        for (row in 0..1) {
            for (col in 0..2) {
                cardViews.add(createCardView(row, col))
            }
        }

        for (row in 0..3) {
            labels.add(createLabel(row))
        }

        addAll(cardViews)
        addAll(labels)
    }

    //------------------------------------------------------------------------------------------------------------------
    override fun refreshAfterStartNewGame() = refreshThisPane()
    override fun refreshAfterFirstReveal() = refreshThisPane()
    override fun refreshAfterReveal() = refreshThisPane()
    override fun refreshOnLastRound() = refreshThisPane()
    override fun refreshAfterDrawCard() = refreshThisPane()
    override fun refreshAfterDrawDiscardedCard() = refreshThisPane()
    override fun refreshAfterSwap() = refreshThisPane()
    override fun refreshAfterDiscard() = refreshThisPane()
    override fun refreshAfterGameEnd() = refreshThisPane()
    //------------------------------------------------------------------------------------------------------------------
    override fun refreshAfterNextTurn() {
        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = { refreshThisPane() }
            }
        )
    }

    override fun refreshBeforeGameEnd() {
        gameScene.playAnimation(
            DelayAnimation(duration = DELAY_BEFORE_REVEAL_ALL).apply {
                onFinished = {
                    rootService.gameService.revealAllCards()
                    rootService.gameService.removeIdenticalRows()
                    refreshThisPane()
                }
            }
        )
    }
}