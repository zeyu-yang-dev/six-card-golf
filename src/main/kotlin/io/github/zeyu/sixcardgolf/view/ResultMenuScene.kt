package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.Refreshable
import io.github.zeyu.sixcardgolf.service.RootService

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
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
    private val placeLabelOffset = 10
    private val allComponentsOffsetY = 40


    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    private val buttonsPosOffsetY = 70

    val restartButton = Button(
        width = 200, height = 75,
        posX = 150, posY = 542.5 + 100 + buttonsPosOffsetY,
        text = "REPLAY",
        font = Font(size = 30, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(131, 184, 24).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
    }

    val exitButton = Button(
        width = 150, height = 56.25,
        posX = 175, posY = 542.5 + 200 + buttonsPosOffsetY,
        text = "EXIT",
        font = Font(size = 22.5, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(197, 0, 45).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
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

    }

    override fun refreshAfterGameEnd() {

        refreshPlayerLabels()
        refreshTitleLabels()
    }
}

