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



class PanePlayerBottom(
    private val rootService: RootService,
    private val gameScene: GameScene
) : Pane<ComponentView>(
    400 + (SCREEN_WIDTH - CARD_WIDTH * 3 - HORIZ_DIS_BTW_CARDS * 2) / 2,
    SCREEN_HEIGHT - PPL_HEIGHT - PPT_DIS_TO_TOP,
    PPL_WIDTH,
    PPL_HEIGHT,
    visual = PLAYER_PANE_BG_VISUAL
), Refreshable {

    private val cardImageLoader = CardImageLoader()

    private val cardViews: MutableList<CardView> = mutableListOf()
    private val labels: MutableList<Label> = mutableListOf()
    private val playerActionService = rootService.playerActionService

    private fun createCardView(row: Int, col: Int): CardView {
        val cardView = CardView(
            posX = (CARD_WIDTH + HORIZ_DIS_BTW_CARDS) * col,
            posY = (CARD_HEIGHT + VERT_DIS_BTW_CARDS) * row,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
            back = ImageVisual(cardImageLoader.backImage)
        ).apply {
            this.showFront()
        }
        return cardView
    }

    private fun createLabel(row: Int): Label {
        val label = Label(
            posX = CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2 + DIS_BTW_LABEL_AND_CARD,
            posY = PLAYER_LABEL_POS_Y + PLAYER_LABEL_HEIGHT * row,
            width = PLAYER_LABEL_WIDTH,
            height = PLAYER_LABEL_HEIGHT,
            text = listOf("Player Name:", "", "Visible Score:", "").getOrElse(row) {""},
            font = Font(size = 25, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD),
            alignment = Alignment.CENTER_LEFT,
        )
        return label
    }

    private fun getPlayerOfThisPane(): Player {
        val currentGame = rootService.currentGame
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val numOfPlayers = currentGame.players.size

        // This pane always shows the deck of the current player.
        val toPlayerOfThisPane = 0

        val playerOfThisPane = currentGame.players[(currentPlayerIndex + toPlayerOfThisPane) % numOfPlayers]
        return playerOfThisPane
    }


    private fun refreshCardViews() {

        val playerOfThisPane = getPlayerOfThisPane()

        val cards = playerOfThisPane.topRow + playerOfThisPane.bottomRow

        for (i in 0..5) {
            cardViews[i].apply {
                val card = cards[i]
                card?.let {
                    this.frontVisual = ImageVisual(cardImageLoader.frontImageFor(card.cardSuit, card.cardValue))
                    if (card.isRevealed) this.showFront() else this.showBack()
                    this.isVisible = true
                } ?: run {
                    // if card is null, this row is removed
                    this.isVisible = false
                }
            }
        }
    }

    private fun refreshLabels() {
        val playerOfThisPane = getPlayerOfThisPane()
        val players = rootService.currentGame.players

        // Select the right color for the player displayed in this pane.
        val font = Font(
            size = 25,
            color = PLAYER_COLORS[players.indexOf(playerOfThisPane)],
            fontWeight = Font.FontWeight.SEMI_BOLD
            )

        labels[1].text = playerOfThisPane.playerName
        labels[1].font = font
        labels[3].text = "${playerOfThisPane.deckScore}"
        labels[3].font = font
    }

    private fun refreshThisPane() {
        refreshCardViews()
        refreshLabels()
    }


    // TODO
    private fun updateInteractivity() {

    }



    init {
        for (row in 0..1) {
            for (col in 0..2) {
                cardViews.add(createCardView(row, col))
            }
        }

        for (row in 0..3) {
            labels.add(createLabel(row))
        }

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

        addAll(cardViews)
        addAll(labels)
    }



    override fun refreshAfterStartNewGame() {
        refreshThisPane()
    }

    override fun refreshAfterFirstReveal() {
        refreshThisPane()
    }

    override fun refreshAfterReveal() {
        refreshThisPane()
    }

    override fun refreshOnLastRound() {
        refreshThisPane()
    }

    override fun refreshAfterDrawCard() {
        refreshThisPane()
    }

    override fun refreshAfterDrawDiscardedCard() {
        refreshThisPane()
    }

    override fun refreshAfterSwap() {
        refreshThisPane()
    }


    override fun refreshAfterDiscard() {
        refreshThisPane()
    }

    override fun refreshAfterNextTurn() {
        gameScene.playAnimation(

            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = {
                    refreshThisPane()
                }
            }
        )
    }

    override fun refreshBeforeGameEnd() {
        refreshThisPane()
    }

    override fun refreshAfterGameEnd() {
        refreshThisPane()
    }


}