package io.github.zeyu.sixcardgolf.view.panes

import io.github.zeyu.sixcardgolf.entity.*
import io.github.zeyu.sixcardgolf.service.*
import io.github.zeyu.sixcardgolf.view.GameScene
import io.github.zeyu.sixcardgolf.view.Refreshable

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

const val SCREEN_WIDTH = 1600
const val SCREEN_HEIGHT = 900

const val CARDS_SCALE = 0.65
const val CARD_WIDTH = 130 * CARDS_SCALE
const val CARD_HEIGHT = 200 * CARDS_SCALE
const val HORIZ_DIS_BTN_CARDS = 5.5
const val VERT_DIS_BTN_CARDS = 4

const val DIS_BTN_LABEL_AND_CARD = 17.75

const val PLAYER_LABEL_WIDTH = 170
const val PLAYER_LABEL_HEIGHT = 40

const val PPL_DIS_TO_LEFT = 80
const val PPL_WIDTH = PLAYER_LABEL_WIDTH + DIS_BTN_LABEL_AND_CARD + CARD_WIDTH * 3 + HORIZ_DIS_BTN_CARDS * 2
const val PPL_HEIGHT = CARD_HEIGHT * 2 + VERT_DIS_BTN_CARDS
const val PPL_POS_X = PPL_DIS_TO_LEFT
const val PPL_POS_Y = (SCREEN_HEIGHT - PPL_HEIGHT) / 2

const val PLAYER_LABEL_POX_Y = (PPL_HEIGHT - PLAYER_LABEL_HEIGHT * 4) / 2






class PanePlayerLeft(
    private val rootService: RootService,
    private val gameScene: GameScene
) : Pane<ComponentView>(
    PPL_POS_X,
    PPL_POS_Y,
    PPL_WIDTH,
    PPL_HEIGHT,
    visual = ColorVisual(50, 50, 50, 90)
), Refreshable {

    private val cardImageLoader = CardImageLoader()

    private val cardViews: MutableList<CardView> = mutableListOf()
    private val labels: MutableList<Label> = mutableListOf()

    private fun createCardView(row: Int, col: Int): CardView {
        val cardView = CardView(
            posX = PLAYER_LABEL_WIDTH + DIS_BTN_LABEL_AND_CARD + (CARD_WIDTH + HORIZ_DIS_BTN_CARDS) * col,
            posY = (CARD_HEIGHT + VERT_DIS_BTN_CARDS) * row,
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
            posX = 0,
            posY = PLAYER_LABEL_POX_Y + PLAYER_LABEL_HEIGHT * row,
            width = PLAYER_LABEL_WIDTH,
            height = PLAYER_LABEL_HEIGHT,
            text = listOf("Player Name:", "", "Visible Score:", "").getOrElse(row) {""},
            font = Font(size = 25, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD),
            alignment = Alignment.CENTER_LEFT,
        )
        return label
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

        addAll(cardViews)
        addAll(labels)
    }




}