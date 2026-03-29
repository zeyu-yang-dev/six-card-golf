package io.github.zeyu.sixcardgolf.view.panes

import io.github.zeyu.sixcardgolf.entity.*
import io.github.zeyu.sixcardgolf.service.*
import io.github.zeyu.sixcardgolf.view.GameScene
import io.github.zeyu.sixcardgolf.view.Refreshable

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.visual.ColorVisual



const val CARDS_SCALE = 0.65
const val CARD_WIDTH = 130 * CARDS_SCALE
const val CARD_HEIGHT = 200 * CARDS_SCALE
const val HORIZ_DIS_BTN_CARDS = 5.5
const val VERT_DIS_BTN_CARDS = 4

const val PLAYER_LABEL_WIDTH = 180
const val PLAYER_LABEL_HEIGHT = 50
const val DIS_BTN_LABEL_AND_CARD = 7.75

const val SCREEN_WIDTH = 1600
const val SCREEN_HEIGHT = 900

const val PPL_DIS_TO_LEFT = 80
const val PPL_WIDTH = PLAYER_LABEL_WIDTH + DIS_BTN_LABEL_AND_CARD + CARD_WIDTH * 3 + HORIZ_DIS_BTN_CARDS * 2
const val PPL_HEIGHT = CARD_HEIGHT * 2 + VERT_DIS_BTN_CARDS
const val PPL_POS_X = PPL_DIS_TO_LEFT
const val PPL_POS_Y = (SCREEN_HEIGHT - PPL_HEIGHT) / 2





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




}