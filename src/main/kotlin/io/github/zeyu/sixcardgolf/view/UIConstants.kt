package io.github.zeyu.sixcardgolf.view

import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

// Size of the scenes:
const val SCREEN_WIDTH = 1920
const val SCREEN_HEIGHT = 1080

const val MENU_SCENE_WIDTH = 600
const val MENU_SCENE_HEIGHT = SCREEN_HEIGHT

//----------------------------------------------------------------------------------------------------------------------

// Constants for player panes:
// PPL stands for PanePlayerLeft, etc.
const val CARDS_SCALE = 0.65
const val CARD_WIDTH = 130 * CARDS_SCALE
const val CARD_HEIGHT = 200 * CARDS_SCALE
const val HORIZ_DIS_BTW_CARDS = 5.5
const val VERT_DIS_BTW_CARDS = 4

const val DIS_BTW_LABEL_AND_CARD = 37.75

const val PLAYER_LABEL_WIDTH = 170
const val PLAYER_LABEL_HEIGHT = 40

const val PPT_DIS_TO_TOP = 18
const val PPL_DIS_TO_LEFT = 60
const val PPL_WIDTH = PLAYER_LABEL_WIDTH + DIS_BTW_LABEL_AND_CARD + CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2
const val PPL_HEIGHT = CARD_HEIGHT * 2 + VERT_DIS_BTW_CARDS
const val PPL_POS_X = PPL_DIS_TO_LEFT
const val PPL_POS_Y = (SCREEN_HEIGHT - PPL_HEIGHT) / 2
const val PLAYER_LABEL_POS_Y = (PPL_HEIGHT - PLAYER_LABEL_HEIGHT * 4) / 2

//----------------------------------------------------------------------------------------------------------------------

// Constants for PaneMiddleCards:
const val PMC_WIDTH = CARD_WIDTH * 3 + HORIZ_DIS_BTW_CARDS * 2
const val PMC_HEIGHT = CARD_HEIGHT
const val PMC_POS_X = (SCREEN_WIDTH - PMC_WIDTH) / 2
const val PMC_POS_Y = (SCREEN_HEIGHT - PMC_HEIGHT) / 2

//----------------------------------------------------------------------------------------------------------------------

// Colors, fonts, delays:
val PLAYER_COLORS = listOf(Color.CYAN, Color.YELLOW, Color.LIGHT_GRAY, Color.MAGENTA)
// val PLAYER_PANE_BG_VISUAL = ColorVisual(50, 50, 50, 90)
val PLAYER_PANE_BG_VISUAL = ColorVisual.TRANSPARENT

val DEFAULT_INSTRUCTION_FONT = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
val PINK_INSTRUCTION_FONT = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)
val GREEN_INSTRUCTION_FONT = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)
val RED_INSTRUCTION_FONT = Font(size = 15, color = Color.RED, fontWeight = Font.FontWeight.SEMI_BOLD)

const val DELAY_BTW_TURNS = 2000
const val DELAY_BEFORE_REVEAL_ALL = 3000

// GameScene and PaneMiddleCards:
const val MIDDLE_LABEL_WIDTH = 300
const val MIDDLE_LABEL_HEIGHT = 50

//----------------------------------------------------------------------------------------------------------------------

// MainMenuScene:
/** The background radius of the button in pixel. */
const val BUTTON_BACKGROUND_RADIUS = 10
const val BTN_ROUND_STYLE = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

const val MMS_PLAYER_LABEL_POS_X = 70
const val MMS_PLAYER_LABEL_POS_Y = 130
const val MMS_PLAYER_LABEL_WIDTH = 150
const val MMS_PLAYER_LABEL_HEIGHT = 50

const val MMS_POS_Y_STEP = 60
const val MMS_TEXT_FIELD_TO_RIGHT = 90

const val MMS_TEXT_FIELD_POS_X = MMS_PLAYER_LABEL_POS_X + MMS_PLAYER_LABEL_WIDTH
const val MMS_TEXT_FIELD_WIDTH = MENU_SCENE_WIDTH - MMS_TEXT_FIELD_POS_X - MMS_TEXT_FIELD_TO_RIGHT

const val ADD_BTN_WIDTH = 250
const val ADD_BTN_HEIGHT = 50
const val ADD_BTN_POS_Y = 400
const val DIS_BTW_ADD_REMOVE = 10
const val REMOVE_BTN_POS_Y = ADD_BTN_POS_Y + ADD_BTN_HEIGHT + DIS_BTW_ADD_REMOVE

val ADD_BTN_FONT = Font(size = 20, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL)
val CHECK_BOX_FONT = Font(size = 20, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL)

const val DIS_BTW_BTN_CB = 32.5
const val CHECK_BOX_BG_WIDTH = 200
const val CHECK_BOX_BG_HEIGHT = 75
const val CHECK_BOX_BG_POS_X = (MENU_SCENE_WIDTH - CHECK_BOX_BG_WIDTH) / 2
const val CHECK_BOX_BG_POS_Y = REMOVE_BTN_POS_Y + ADD_BTN_HEIGHT + DIS_BTW_BTN_CB // 542.5

const val CHECK_BOX_HEIGHT = 50
const val CHECK_BOX_POS_X = (MENU_SCENE_WIDTH - CHECK_BOX_BG_WIDTH) / 2 + 25

const val START_BTN_POS_Y = CHECK_BOX_BG_POS_Y + CHECK_BOX_BG_HEIGHT + DIS_BTW_BTN_CB // 650

const val MMS_EXIT_BTN_WIDTH = 150
const val MMS_EXIT_BTN_HEIGHT = 56.25
const val MMS_EXIT_BTN_POS_Y = START_BTN_POS_Y + CHECK_BOX_BG_HEIGHT + 17.5