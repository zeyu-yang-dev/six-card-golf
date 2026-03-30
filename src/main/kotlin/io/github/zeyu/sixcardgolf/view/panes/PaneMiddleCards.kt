package io.github.zeyu.sixcardgolf.view.panes

import io.github.zeyu.sixcardgolf.entity.*
import io.github.zeyu.sixcardgolf.service.*
import io.github.zeyu.sixcardgolf.view.*

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
    PMC_POS_X,
    PMC_POS_Y,
    PMC_WIDTH,
    PMC_HEIGHT,
    visual = PLAYER_PANE_BG_VISUAL
), Refreshable {

    private val cardImageLoader = CardImageLoader()

    private val cardViews: MutableList<CardView> = mutableListOf()

    private fun createCardView(col: Int): CardView {
        val cardView = CardView(
            posX = (CARD_WIDTH + HORIZ_DIS_BTW_CARDS) * col,
            posY = 0,
            width = CARD_WIDTH,
            height = CARD_HEIGHT,
            front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
            back = ImageVisual(cardImageLoader.backImage)
        ).apply {
            this.showFront()
        }
        return cardView
    }




    init {
        for (col in 0..2) {
            cardViews.add(createCardView(col))
        }

        addAll(cardViews)

    }





}