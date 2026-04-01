package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.Refreshable
import io.github.zeyu.sixcardgolf.service.RootService

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * The End Menu.
 */
class ResultMenuScene(
    private val rootService: RootService
) : MenuScene(
    width = MENU_SCENE_WIDTH,
    height = MENU_SCENE_HEIGHT
), Refreshable {

    private val playerLabels: MutableList<Label> = mutableListOf()
    private val titleLabels: MutableList<Label> = mutableListOf()

    val restartButton = Button(
        width = RMS_REPLAY_BTN_WIDTH,
        height = RMS_REPLAY_BTN_HEIGHT,
        posX = (MENU_SCENE_WIDTH - RMS_REPLAY_BTN_WIDTH) / 2,
        posY = RMS_REPLAY_BTN_POS_Y,
        text = "REPLAY",
        font = START_BTN_FONT,
        visual = START_BTN_COLOR_VISUAL.apply { style = BTN_ROUND_STYLE }
    )

    val exitButton = Button(
        width = RMS_EXIT_BTN_WIDTH,
        height = RMS_EXIT_BTN_HEIGHT,
        posX = (MENU_SCENE_WIDTH - RMS_EXIT_BTN_WIDTH) / 2,
        posY = RMS_REPLAY_BTN_POS_Y + RMS_REPLAY_BTN_HEIGHT + DIS_BTW_REPLAY_EXIT_BTN,
        text = "EXIT",
        font = EXIT_BTN_FONT,
        visual = EXIT_BTN_COLOR_VISUAL.apply { style = BTN_ROUND_STYLE }
    )
    //------------------------------------------------------------------------------------------------------------------
    private fun createPlayerLabel(row: Int): Label {
        val label = Label(
            width = RMS_PLAYER_LABEL_WIDTH,
            height = RMS_PLAYER_LABEL_HEIGHT,
            posX = RMS_PLAYER_LABEL_POS_X,
            posY = RMS_PLAYER_LABEL_POS_Y + (RMS_PLAYER_LABEL_HEIGHT + RMS_PLAYER_LABEL_SPACER) * row,
            font = Font(size = 17),
            visual = RMS_PLAYER_LABEL_BG_VISUAL
        )
        return label
    }

    private fun createTitleLabel(row: Int): Label {
        val label = Label(
            width = RMS_TITLE_LABEL_WIDTH,
            height = RMS_TITLE_LABEL_HEIGHT,
            posX = RMS_TITLE_LABEL_POS_X,
            posY = RMS_TITLE_LABEL_POS_Y + (RMS_PLAYER_LABEL_HEIGHT + RMS_PLAYER_LABEL_SPACER) * row,
            text = RMS_TITLE_LABEL_TEXT[row],
            font = Font(
                size = 25,
                color = RMS_TITLE_LABEL_COLORS[row],
                fontWeight = Font.FontWeight.SEMI_BOLD
            ),
            visual = RMS_TITLE_LABEL_BG_VISUAL
        )
        return label
    }

    private fun refreshPlayerLabels() {

        val orderedPlayers = rootService.gameService.orderPlayers()

        // Hide all player labels
        playerLabels.forEach { it.isVisible = false }

        // Display the results for every player
        for (i in orderedPlayers.indices) {
            // Make player labels, which actually has a player, visible
            playerLabels[i].isVisible = true
            playerLabels[i].text = orderedPlayers[i].toString()
        }
    }

    private fun refreshTitleLabels() {

        val numOfPlayers = rootService.currentGame.players.size

        // Hide all title labels
        titleLabels.forEach { it.isVisible = false }

        // Display the titles for every player
        for (i in 0..(numOfPlayers - 2)) titleLabels[i].isVisible = true
    }
    //------------------------------------------------------------------------------------------------------------------
    init {
        val image : BufferedImage = ImageIO.read(javaClass.getResource("/result_background.png"))
        this.background = ImageVisual(image)
        opacity = 1.0

        // Add all player labels:
        for (row in 0..3) {
            playerLabels.add(createPlayerLabel(row))
            addComponents(playerLabels[row])
        }

        // Add all title labels:
        for (row in 0..2) {
            titleLabels.add(createTitleLabel(row))
            addComponents(titleLabels[row])
        }

        // Add buttons:
        addComponents(
            restartButton,
            exitButton
        )
    }

    override fun refreshAfterGameEnd() {
        refreshPlayerLabels()
        refreshTitleLabels()
    }
}

