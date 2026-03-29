package io.github.zeyu.sixcardgolf.view

import java.awt.Color


const val SCREEN_WIDTH = 1600
const val SCREEN_HEIGHT = 900

// components in a player pane:
const val CARDS_SCALE = 0.65
const val CARD_WIDTH = 130 * CARDS_SCALE
const val CARD_HEIGHT = 200 * CARDS_SCALE
const val HORIZ_DIS_BTW_CARDS = 5.5
const val VERT_DIS_BTW_CARDS = 4

const val DIS_BTW_LABEL_AND_CARD = 17.75

const val PLAYER_LABEL_WIDTH = 170
const val PLAYER_LABEL_HEIGHT = 40

// player pane at left side of the screen:
const val PPL_DIS_TO_LEFT = 80
const val PPL_WIDTH = PLAYER_LABEL_WIDTH + DIS_BTW_LABEL_AND_CARD + CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2
const val PPL_HEIGHT = CARD_HEIGHT * 2 + VERT_DIS_BTW_CARDS
const val PPL_POS_X = PPL_DIS_TO_LEFT
const val PPL_POS_Y = (SCREEN_HEIGHT - PPL_HEIGHT) / 2
const val PLAYER_LABEL_POS_Y = (PPL_HEIGHT - PLAYER_LABEL_HEIGHT * 4) / 2

val PLAYER_COLORS = listOf(Color.CYAN, Color.YELLOW, Color.LIGHT_GRAY, Color.MAGENTA)

const val DELAY_BTW_TURNS = 2000