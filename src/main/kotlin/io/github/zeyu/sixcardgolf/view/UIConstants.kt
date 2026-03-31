package io.github.zeyu.sixcardgolf.view

import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color


const val SCREEN_WIDTH = 1600
const val SCREEN_HEIGHT = 900

// components in a player pane:
const val CARDS_SCALE = 0.65
const val CARD_WIDTH = 130 * CARDS_SCALE
const val CARD_HEIGHT = 200 * CARDS_SCALE
const val HORIZ_DIS_BTW_CARDS = 5.5
const val VERT_DIS_BTW_CARDS = 4

const val DIS_BTW_LABEL_AND_CARD = 37.75

const val PLAYER_LABEL_WIDTH = 170
const val PLAYER_LABEL_HEIGHT = 40

// Constants for player panes:
// PPL stands for PanePlayerLeft, etc.
const val PPT_DIS_TO_TOP = 18
const val PPL_DIS_TO_LEFT = 60
const val PPL_WIDTH = PLAYER_LABEL_WIDTH + DIS_BTW_LABEL_AND_CARD + CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2
const val PPL_HEIGHT = CARD_HEIGHT * 2 + VERT_DIS_BTW_CARDS
const val PPL_POS_X = PPL_DIS_TO_LEFT
const val PPL_POS_Y = (SCREEN_HEIGHT - PPL_HEIGHT) / 2
const val PLAYER_LABEL_POS_Y = (PPL_HEIGHT - PLAYER_LABEL_HEIGHT * 4) / 2

const val PMC_WIDTH = CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2
const val PMC_HEIGHT = CARD_HEIGHT
const val PMC_POS_X = (SCREEN_WIDTH - PMC_WIDTH) / 2
const val PMC_POS_Y = (SCREEN_HEIGHT - PMC_HEIGHT) / 2

val PLAYER_COLORS = listOf(Color.CYAN, Color.YELLOW, Color.LIGHT_GRAY, Color.MAGENTA)
// val PLAYER_PANE_BG_VISUAL = ColorVisual(50, 50, 50, 90)
val PLAYER_PANE_BG_VISUAL = ColorVisual.TRANSPARENT

val DEFAULT_INSTRUCTION_FONT = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
val PINK_INSTRUCTION_FONT = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)
val GREEN_INSTRUCTION_FONT = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)
val RED_INSTRUCTION_FONT = Font(size = 15, color = Color.RED, fontWeight = Font.FontWeight.SEMI_BOLD)

const val DELAY_BTW_TURNS = 2000
const val DELAY_BEFORE_REVEAL_ALL = 3000

const val MIDDLE_LABEL_WIDTH = 300
const val MIDDLE_LABEL_HEIGHT = 50