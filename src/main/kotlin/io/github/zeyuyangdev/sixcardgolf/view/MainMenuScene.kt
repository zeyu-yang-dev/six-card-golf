package io.github.zeyuyangdev.sixcardgolf.view

import io.github.zeyuyangdev.sixcardgolf.service.Refreshable
import io.github.zeyuyangdev.sixcardgolf.service.RootService

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.uicomponents.Button

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * The Start Menu.
 */
class MainMenuScene(
    private val rootService: RootService
) : MenuScene(
    width = MENU_SCENE_WIDTH,
    height = MENU_SCENE_HEIGHT
), Refreshable {

    val playerLabels: MutableList<Label> = mutableListOf()
    val textFields: MutableList<TextField> = mutableListOf()

    //------------------------------------------------------------------------------------------------------------------
    private val addPlayer: Button = Button(
        posX = (MENU_SCENE_WIDTH - ADD_BTN_WIDTH) / 2,
        posY = ADD_BTN_POS_Y,
        width = ADD_BTN_WIDTH,
        height = ADD_BTN_HEIGHT,
        text = "ADD PLAYER",
        font = ADD_BTN_FONT,
        visual = ColorVisual(102, 172, 218).apply { style = BTN_ROUND_STYLE }
    ).apply {
        onMouseClicked = {
            // If there are 2 rows now:
            if (!components.contains(playerLabels[2])) {
                addComponents(
                    textFields[2].apply { text = "Paul" },
                    playerLabels[2]
                )
                removePlayer.isDisabled = false
                startButton.isDisabled = shouldDisableStartButton()
            } else {
                // If there are 3 rows now:
                addComponents(
                    textFields[3].apply { text = "Detlef" },
                    playerLabels[3]
                )
                this.isDisabled = true
                startButton.isDisabled = shouldDisableStartButton()
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private val removePlayer: Button = Button(
        posX = (MENU_SCENE_WIDTH - ADD_BTN_WIDTH) / 2,
        posY = REMOVE_BTN_POS_Y,
        width = ADD_BTN_WIDTH,
        height = ADD_BTN_HEIGHT,
        text = "REMOVE PLAYER",
        font = ADD_BTN_FONT,
        visual = ColorVisual(102, 172, 218).apply { style = BTN_ROUND_STYLE }
    ).apply {
        isDisabled = true
        onMouseClicked = {
            // If there are 4 rows now:
            if (components.contains(playerLabels[3])) {
                removeComponents(
                    textFields[3],
                    playerLabels[3]
                )
                addPlayer.isDisabled = false
                startButton.isDisabled = shouldDisableStartButton()
            } else {
                // If there are 3 rows now:
                removeComponents(
                    textFields[2],
                    playerLabels[2]
                )
                addPlayer.isDisabled = false
                startButton.isDisabled = shouldDisableStartButton()
                this.isDisabled = true
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    private val checkBoxBackGround = Label(
        width = CHECK_BOX_BG_WIDTH,
        height = CHECK_BOX_BG_HEIGHT,
        posX = CHECK_BOX_BG_POS_X,
        posY = CHECK_BOX_BG_POS_Y,
        visual = ColorVisual(211, 211, 211).apply { style = BTN_ROUND_STYLE }
    ).apply { opacity = 0.6 }

    // Select whether the player order should be randomized:
    private val randomizeCheckBox = CheckBox(
        width = CHECK_BOX_BG_WIDTH,
        height = CHECK_BOX_HEIGHT,
        posX = CHECK_BOX_POS_X,
        posY = CHECK_BOX_BG_POS_Y - 2.5,
        text = "RANDOMIZE",
        alignment = Alignment.CENTER_LEFT, // the position of the entire component in the area
        font = CHECK_BOX_FONT
    )

    // Select whether the test mode should be activated:
    private val testModeCheckBox = CheckBox(
        width = CHECK_BOX_BG_WIDTH,
        height = CHECK_BOX_HEIGHT,
        posX = CHECK_BOX_POS_X,
        posY = CHECK_BOX_BG_POS_Y + 27.5,
        text = "TEST MODE",
        alignment = Alignment.CENTER_LEFT, // the position of the entire component in the area
        font = CHECK_BOX_FONT
    )
    //------------------------------------------------------------------------------------------------------------------
    private val startButton = Button(
        width = CHECK_BOX_BG_WIDTH,
        height = CHECK_BOX_BG_HEIGHT,
        posX = (MENU_SCENE_WIDTH - CHECK_BOX_BG_WIDTH) / 2,
        posY = START_BTN_POS_Y,
        text = "START",
        font = START_BTN_FONT,
        visual = START_BTN_COLOR_VISUAL.apply { style = BTN_ROUND_STYLE }
    ).apply {
        onMouseClicked = {

            // Add the unser input for player names to a list:
            val playerNames: MutableList<String> = mutableListOf(textFields[0].text, textFields[1].text)
            if (components.contains(textFields[2])) playerNames.add(textFields[2].text)
            if (components.contains(textFields[3])) playerNames.add(textFields[3].text)

            // Randomize the order of playerNames if needed:
            if (randomizeCheckBox.isChecked) playerNames.shuffle()

            // Start a new game using [GameService.startNewGame]:
            rootService.gameService.startNewGame(playerNames, testModeCheckBox.isChecked)
        }
    }

    val exitButton = Button(
        width = MMS_EXIT_BTN_WIDTH,
        height = MMS_EXIT_BTN_HEIGHT,
        posX = (MENU_SCENE_WIDTH - MMS_EXIT_BTN_WIDTH) / 2,
        posY = MMS_EXIT_BTN_POS_Y,
        text = "EXIT",
        font = EXIT_BTN_FONT,
        visual = EXIT_BTN_COLOR_VISUAL.apply { style = BTN_ROUND_STYLE }
    )
    //------------------------------------------------------------------------------------------------------------------

    private fun shouldDisableStartButton(): Boolean {

        val inputs = textFields

        val inputsHaveBlank = inputs.any { it.text.isBlank() }
        val inputsAreTooLong = inputs.any { it.text.length > 20 }
        val inputsHaveDuplicate = inputs.map { it.text }.toSet().size != inputs.size

        return inputsHaveBlank || inputsAreTooLong || inputsHaveDuplicate
    }

    private fun createPlayerLabel(row: Int): Label {
        val playerLabel = Label(
            posX = MMS_PLAYER_LABEL_POS_X,
            posY = MMS_PLAYER_LABEL_POS_Y + MMS_POS_Y_STEP * row,
            width = MMS_PLAYER_LABEL_WIDTH,
            height = MMS_PLAYER_LABEL_HEIGHT,
            text = "PLAYER ${row + 1}:",
            font = Font(size = 25)
        )
        return playerLabel
    }

    private fun createTextField(row: Int): TextField {
        val textField = TextField(
            posX = MMS_TEXT_FIELD_POS_X,
            posY = MMS_PLAYER_LABEL_POS_Y + MMS_POS_Y_STEP * row,
            width = MMS_TEXT_FIELD_WIDTH,
            height = MMS_PLAYER_LABEL_HEIGHT,
            text = listOf("Martin", "Erich", "Paul", "Detlef").getOrElse(row) { "" },
            font = Font(size = 25)
        ).apply {
            onKeyReleased = { startButton.isDisabled = shouldDisableStartButton() }
        }
        return textField
    }
    //------------------------------------------------------------------------------------------------------------------

    init {

        val image : BufferedImage = ImageIO.read(javaClass.getResource("/menu_background.png"))
        this.background = ImageVisual(image)
        opacity = 1.0

        for(i in 0..3) {
            playerLabels.add(createPlayerLabel(i))
            textFields.add(createTextField(i))
        }

        for(i in 0..1) {
            addComponents(
                playerLabels[i],
                textFields[i],
            )
        }

        addComponents(
            checkBoxBackGround,
            randomizeCheckBox,
            testModeCheckBox,
            addPlayer,
            removePlayer,
            startButton,
            exitButton
        )
    }
}
