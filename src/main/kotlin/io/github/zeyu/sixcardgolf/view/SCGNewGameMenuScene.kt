package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.CardImageLoader
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

/**
 * The Start Menu.
 */
class SCGNewGameMenuScene(private val rootService: RootService) : MenuScene(width = 500, height = 900), Refreshable {


    private fun shouldDisableButton(): Boolean {

        val inputs = listOf(p1Input, p2Input, p3Input, p4Input)

        val inputsHaveBlank = inputs.any { it.text.isBlank() }
        val inputsAreTooLong = inputs.any { it.text.length > 20 }
        val inputsHaveDuplicate = inputs.map { it.text }.toSet().size != inputs.size

        return inputsHaveBlank || inputsAreTooLong || inputsHaveDuplicate
    }



    //--------------------------------------------------------------------------------------------
    private val playerLabelWidth = 150
    private val textFieldPosX = 150
    private val posYStep = 60
    private val spacerOffset = 10
    private val allComponentsOffsetY = 30


    // Label and input field for player 1:
    private val p1Label = Label(
        width = playerLabelWidth, height = 50,
        posX = 0, posY = 100 + allComponentsOffsetY,
        text = "PLAYER 1:",
        font = Font(size = 25)
    )

    private val p1Input: TextField = TextField(
        width = 500 - playerLabelWidth - spacerOffset, height = 50,
        posX = textFieldPosX, posY = 100 + allComponentsOffsetY,
        text = "Martin",
        font = Font(size = 25)
    ).apply {
        onKeyReleased = { startButton.isDisabled = shouldDisableButton() }
    }

    //--------------------------------------------------------------------------------------------

    // Label and input field for player 2:
    private val p2Label = Label(
        width = playerLabelWidth, height = 50,
        posX = 0, posY = 100 + posYStep + allComponentsOffsetY,
        text = "PLAYER 2:",
        font = Font(size = 25)
    )

    private val p2Input: TextField = TextField(
        width = 500 - playerLabelWidth - spacerOffset, height = 50,
        posX = textFieldPosX, posY = 100 + posYStep + allComponentsOffsetY,
        text = "Erich",
        font = Font(size = 25)
    ).apply {
        onKeyReleased = { startButton.isDisabled = shouldDisableButton() }
    }

    //--------------------------------------------------------------------------------------------

    // Label and input field for player 3:
    private val p3Label = Label(
        width = playerLabelWidth, height = 50,
        posX = 0, posY = 100 + 2 * posYStep + allComponentsOffsetY,
        text = "PLAYER 3:",
        font = Font(size = 25)
    )

    private val p3Input: TextField = TextField(
        width = 500 - playerLabelWidth - spacerOffset, height = 50,
        posX = textFieldPosX, posY = 100 + 2 * posYStep + allComponentsOffsetY,
        text = "Paul",
        font = Font(size = 25)
    ).apply {
        onKeyReleased = { startButton.isDisabled = shouldDisableButton() }
    }

    //--------------------------------------------------------------------------------------------

    // Label and input field for player 4:
    private val p4Label = Label(
        width = playerLabelWidth, height = 50,
        posX = 0, posY = 100 + 3 * posYStep + allComponentsOffsetY,
        text = "PLAYER 4:",
        font = Font(size = 25)
    )

    private val p4Input: TextField = TextField(
        width = 500 - playerLabelWidth - spacerOffset, height = 50,
        posX = textFieldPosX, posY = 100 + 3 * posYStep + allComponentsOffsetY,
        text = "Detlef",
        font = Font(size = 25)
    ).apply {
        onKeyReleased = { startButton.isDisabled = shouldDisableButton() }
    }

    //--------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------
    private val addPlayer: Button = Button(
        width = 250, height = 50,
        posX = 125, posY = 370 + allComponentsOffsetY,
        text = "ADD PLAYER",
        font = Font(size = 20),
        visual = ColorVisual(102, 172, 218).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

        onMouseClicked = {
            if (!components.contains(p3Label)) {
                addComponents(
                    p3Input.apply { text = "Paul" },
                    p3Label
                )
                removePlayer.isDisabled = false
                startButton.isDisabled = shouldDisableButton()
            } else {
                addComponents(
                    p4Input.apply { text = "Detlef" },
                    p4Label
                )
                this.isDisabled = true
                startButton.isDisabled = shouldDisableButton()
            }
        }
    }

    //--------------------------------------------------------------------------------------------
    private val removePlayer: Button = Button(
        width = 250, height = 50,
        posX = 125, posY = 430 + allComponentsOffsetY,
        text = "REMOVE PLAYER",
        font = Font(size = 20),
        visual = ColorVisual(102, 172, 218).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"

        isDisabled = true
        onMouseClicked = {
            if (components.contains(p4Label)) {
                removeComponents(p4Input.apply { text = "Detlef" }, p4Label)
                addPlayer.isDisabled = shouldDisableButton()
                startButton.isDisabled = shouldDisableButton()
            } else {
                removeComponents(p3Input.apply { text = "Paul" }, p3Label)
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
        posX = 150 + 25, posY = 500 + allComponentsOffsetY + 10,
        text = "RANDOMIZE",
        alignment = Alignment.CENTER_LEFT, // the position of the entire component in the area
        font = Font(size = 20),

    )


    // Select whether the test mode should be activated:
    private val testModeCheckBox = CheckBox(
        width = 200, height = 50,
        posX = 150 + 25, posY = 550 + allComponentsOffsetY - 10,
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
            val playerNames: MutableList<String> = mutableListOf(p1Input.text, p2Input.text)
            if (components.contains(p3Input)) playerNames.add(p3Input.text)
            if (components.contains(p4Input)) playerNames.add(p4Input.text)

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

    init {

        val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource("/menu_background.png"))
        this.background = ImageVisual(image)

        opacity = 1.0

        addComponents(
            checkBoxBackGround,
            randomizeCheckBox,
            testModeCheckBox,

            p1Label, p1Input,
            p2Label, p2Input,
            addPlayer, removePlayer,
            startButton,
            exitButton
        )
    }
}
