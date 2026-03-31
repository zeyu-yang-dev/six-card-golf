package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.CardImageLoader
import io.github.zeyu.sixcardgolf.service.Refreshable
import io.github.zeyu.sixcardgolf.service.RootService

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.uicomponents.Button
import java.awt.Color

import java.awt.image.BufferedImage
import javax.imageio.ImageIO



/** The background radius of the button in pixel. */
const val BUTTON_BACKGROUND_RADIUS = 10

const val MMS_PLAYER_LABEL_POS_X = 0
const val MMS_PLAYER_LABEL_POS_Y = 130
const val MMS_PLAYER_LABEL_WIDTH = 150
const val MMS_PLAYER_LABEL_HEIGHT = 50


const val MMS_POS_Y_STEP = 60
const val MMS_TEXT_FIELD_TO_RIGHT = 10

const val MMS_TEXT_FIELD_POS_X = MMS_PLAYER_LABEL_WIDTH
const val MMS_TEXT_FIELD_WIDTH = 500 - MMS_PLAYER_LABEL_WIDTH - MMS_TEXT_FIELD_TO_RIGHT





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







    //--------------------------------------------------------------------------------------------
    private val addPlayer: Button = Button(
        width = 250, height = 50,
        posX = 125, posY = 370 + 30,
        text = "ADD PLAYER",
        font = Font(size = 20),
        visual = ColorVisual(102, 172, 218).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        // componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

        onMouseClicked = {
            if (!components.contains(playerLabels[2])) {
                addComponents(
                    textFields[2].apply { text = "Paul" },
                    playerLabels[2]
                )
                removePlayer.isDisabled = false
                startButton.isDisabled = shouldDisableButton()
            } else {
                addComponents(
                    textFields[3].apply { text = "Detlef" },
                    playerLabels[3]
                )
                this.isDisabled = true
                startButton.isDisabled = shouldDisableButton()
            }
        }
    }

    //--------------------------------------------------------------------------------------------
    private val removePlayer: Button = Button(
        width = 250, height = 50,
        posX = 125, posY = 430 + 30,
        text = "REMOVE PLAYER",
        font = Font(size = 20),
        visual = ColorVisual(102, 172, 218).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

        isDisabled = true
        onMouseClicked = {
            if (components.contains(playerLabels[3])) {
                removeComponents(textFields[3].apply { text = "Detlef" }, playerLabels[3])
                addPlayer.isDisabled = shouldDisableButton()
                startButton.isDisabled = shouldDisableButton()
            } else {
                removeComponents(textFields[2].apply { text = "Paul" }, playerLabels[2])
                addPlayer.isDisabled = shouldDisableButton()
                startButton.isDisabled = shouldDisableButton()
                this.isDisabled = true
            }
        }
    }


    //--------------------------------------------------------------------------------------------
    // Select whether the player order should be randomized:
    private val randomizeCheckBox = CheckBox(
        width = 200, height = 50,
        posX = 150 + 25, posY = 500 + 30 + 10,
        text = "RANDOMIZE",
        alignment = Alignment.CENTER_LEFT, // the position of the entire component in the area
        font = Font(size = 20),

    )

    // Select whether the test mode should be activated:
    private val testModeCheckBox = CheckBox(
        width = 200, height = 50,
        posX = 150 + 25, posY = 550 + 30 - 10,
        text = "TEST MODE",
        alignment = Alignment.CENTER_LEFT, // the position of the entire component in the area
        font = Font(size = 20),

    )

    private val checkBoxBackGround = Label(
        width = 200, height = 75,
        posX = 150, posY = 542.5,
        visual = ColorVisual(211, 211, 211).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        this.opacity = 0.6
    }
    //--------------------------------------------------------------------------------------------



    private val startButton = Button(
        width = 200, height = 75,
        posX = 150, posY = 542.5 + 100,
        text = "START",
        font = Font(size = 30, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(131, 184, 24).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

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
        width = 150, height = 56.25,
        posX = 175, posY = 542.5 + 200,
        text = "EXIT",
        font = Font(size = 22.5, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(197, 0, 45).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
    }



    //--------------------------------------------------------------------------------------------

    private fun shouldDisableButton(): Boolean {

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
            onKeyReleased = { startButton.isDisabled = shouldDisableButton() }
        }
        return textField
    }






    //--------------------------------------------------------------------------------------------

    init {

        val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource("/menu_background.png"))
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



            addPlayer, removePlayer,
            startButton,
            exitButton
        )

    }
}
